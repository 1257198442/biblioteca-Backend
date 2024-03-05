package com.example.demo.adapters.rest;

import com.example.demo.adapters.rest.dto.TransactionRecordDto;
import com.example.demo.domain.exceptions.ForbiddenException;
import com.example.demo.domain.exceptions.UnprocessableEntityException;
import com.example.demo.domain.models.Role;
import com.example.demo.domain.models.TransactionRecord;
import com.example.demo.domain.models.Wallet;
import com.example.demo.domain.service.TransactionRecordService;
import com.example.demo.domain.service.WalletService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(WalletResource.WALLET)
public class WalletResource {
    public static final String WALLET = "/wallet";
    public static final String TELEPHONE = "/{telephone}";
    public static final String  RECHARGE = "/recharge";
    public final WalletService walletService;
    private final TransactionRecordService transactionRecordService;
    public final List<Role> allRole= Arrays.asList(Role.CLIENT,Role.ADMINISTRATOR,Role.ROOT);
    public final List<Role> adminRole=Arrays.asList(Role.ADMINISTRATOR,Role.ROOT);
    public final List<Role> rootRole= List.of(Role.ROOT);
    @Autowired
    public WalletResource(WalletService walletService,
                          TransactionRecordService transactionRecordService){
        this.walletService = walletService;
        this.transactionRecordService = transactionRecordService;
    }
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(TELEPHONE)
    public Wallet readByTelephone(@PathVariable String telephone){
        if(hasPermission(rootRole,telephone)){
            return this.walletService.readByTelephone(telephone);
        } else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(RECHARGE)
    public TransactionRecord recharge(@RequestBody TransactionRecordDto transactionRecordData){
        if(transactionRecordData.amount.compareTo(BigDecimal.ZERO)<0){
            throw new UnprocessableEntityException("Unable to recharge negative numbers");
        }
        if(hasPermission(rootRole,transactionRecordData.getTelephone())){
        return this.transactionRecordService.create(transactionRecordData);
        }else {
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
