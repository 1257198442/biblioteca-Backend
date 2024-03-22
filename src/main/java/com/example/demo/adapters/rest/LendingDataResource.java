package com.example.demo.adapters.rest;


import com.example.demo.adapters.rest.dto.LendingDataUploadDto;
import com.example.demo.domain.exceptions.ForbiddenException;
import com.example.demo.domain.exceptions.UnauthorizedException;
import com.example.demo.domain.models.LendingData;
import com.example.demo.domain.models.Role;
import com.example.demo.domain.service.LendingDataService;
import com.example.demo.domain.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(LendingDataResource.LENDING_DATA)
public class LendingDataResource {
    public static final String LENDING_DATA = "/lendingData";
    public static final String REFERENCE = "/{reference}";
    public static final String SEARCH = "/search";
    public final LendingDataService lendingDataService;
    public final UserService userService;
    public final List<Role> adminRole= Arrays.asList(Role.ADMINISTRATOR,Role.ROOT);

    @Autowired
    public LendingDataResource(LendingDataService lendingDataService,
                               UserService userService){
        this.lendingDataService = lendingDataService;
        this.userService = userService;
    }

    //POST
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')  or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public LendingData create(@RequestBody LendingDataUploadDto lendingData){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (hasPermission(adminRole,lendingData.getTelephone())) {
            if(!encoder.matches(lendingData.getPassword(),this.userService.getUserPassword(lendingData.getTelephone()))){
                throw new UnauthorizedException("The password is wrong.");
            }
            return this.lendingDataService.create(lendingData);
        }else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }

    //GET
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')  or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(REFERENCE)
    public LendingData read(@PathVariable String reference){
        LendingData lending = this.lendingDataService.read(reference);
        if(hasPermission(adminRole,lending.getUser().getTelephone())){
            return lending;
        }else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')  or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(SEARCH)
    public List<LendingData> readByTelephone(@RequestParam String telephone){
        if(hasPermission(adminRole,telephone)){
            return this.lendingDataService.readAllByUserTelephone(telephone);
        }else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')  or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping()
    public List<LendingData> raedAll(){
        if(Role.isCompetent(adminRole,this.extractRoleClaims())){
            return this.lendingDataService.readAll();
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
