package com.example.demo.adapters.rest;

import com.example.demo.domain.exceptions.ForbiddenException;
import com.example.demo.domain.models.LendingData;
import com.example.demo.domain.models.ReturnData;
import com.example.demo.domain.models.Role;
import com.example.demo.domain.service.LendingDataService;
import com.example.demo.domain.service.ReturnDataService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ReturnDataResource.RETURN_DATA)
public class ReturnDataResource {
    public static final String RETURN_DATA = "/returnData";
    public static final String REFERENCE = "/{reference}";
    private final ReturnDataService returnDataService;
    private final LendingDataService lendingDataService;
    public final List<Role> adminRole= Arrays.asList(Role.ADMINISTRATOR,Role.ROOT);
    @Autowired
    public ReturnDataResource(ReturnDataService returnDataService,
                              LendingDataService lendingDataService){
        this.returnDataService = returnDataService;
        this.lendingDataService = lendingDataService;
    }

    //POST
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')  or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ReturnData create(@RequestBody String reference){
        LendingData lending = this.lendingDataService.read(reference);
        if(hasPermission(adminRole,lending.getUser().getTelephone())){
            return this.returnDataService.create(reference);
        }else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }

    }

    //GET
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')  or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(REFERENCE)
    public ReturnData read(@PathVariable String reference){
        ReturnData restitution = this.returnDataService.read(reference);
        if(hasPermission(adminRole,restitution.getUser().getTelephone())){
        return this.returnDataService.read(reference);
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
