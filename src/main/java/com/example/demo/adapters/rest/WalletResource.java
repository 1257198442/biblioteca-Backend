package com.example.demo.adapters.rest;

import com.example.demo.domain.exceptions.ForbiddenException;
import com.example.demo.domain.models.Role;
import com.example.demo.domain.models.Wallet;
import com.example.demo.domain.service.WalletService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(WalletResource.WALLET)
public class WalletResource {
    public static final String WALLET = "/wallet";
    public static final String TELEPHONE = "/{telephone}";
    public final WalletService walletService;
    public final List<Role> allRole= Arrays.asList(Role.CLIENT,Role.ADMINISTRATOR,Role.ROOT);
    public final List<Role> adminRole=Arrays.asList(Role.ADMINISTRATOR,Role.ROOT);
    public final List<Role> rootRole= List.of(Role.ROOT);
    @Autowired
    public WalletResource(WalletService walletService){
        this.walletService = walletService;
    }
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(TELEPHONE)
    public Wallet readByTelephone(@PathVariable String telephone){
        if(Role.isCompetent(rootRole,this.extractRoleClaims())||extractUserName().equals(telephone)){
            return this.walletService.readByTelephone(telephone);
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

}
