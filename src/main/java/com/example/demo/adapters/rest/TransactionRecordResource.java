package com.example.demo.adapters.rest;

import com.example.demo.domain.exceptions.ForbiddenException;
import com.example.demo.domain.models.Role;
import com.example.demo.domain.models.TransactionRecord;
import com.example.demo.domain.service.TransactionRecordService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(TransactionRecordResource.TRANSACTION)
public class TransactionRecordResource {
    public static final String TRANSACTION = "/transaction";
    public static final String REFERENCE = "/{reference}";
    public static final String SEARCH = "/search";

    private final TransactionRecordService transactionRecordService;
    public final List<Role> rootRole= List.of(Role.ROOT);

    @Autowired
    public TransactionRecordResource(TransactionRecordService transactionRecordService){
        this.transactionRecordService = transactionRecordService;
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(REFERENCE)
    public TransactionRecord readByReference(@PathVariable String reference){
        TransactionRecord transactionRecord = this.transactionRecordService.readByReference(reference);
        if(hasPermission(rootRole,transactionRecord.getTelephone())){
            return transactionRecord;
        } else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(SEARCH)
    public List<TransactionRecord> readByTelephone(@RequestParam String telephone){
        if(hasPermission(rootRole,telephone)){
            return this.transactionRecordService.readByTelephone(telephone);
        } else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }

    }

    private Role extractRoleClaims() {
        List< String > roleClaims = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return Role.of(roleClaims.get(0));
    }

    private String extractUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public boolean hasPermission(List<Role> requiredRoles, String targetTelephone) {
        boolean hasRolePermission = Role.isCompetent(requiredRoles, this.extractRoleClaims());
        boolean isTargetUser = extractUserName().equals(targetTelephone);
        return hasRolePermission || isTargetUser;
    }

}