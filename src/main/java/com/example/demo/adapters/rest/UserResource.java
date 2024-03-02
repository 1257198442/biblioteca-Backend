package com.example.demo.adapters.rest;


import com.example.demo.adapters.rest.dto.TokenDto;
import com.example.demo.adapters.rest.dto.UserUpdateDto;
import com.example.demo.adapters.rest.dto.UserUploadDto;
import com.example.demo.domain.exceptions.ForbiddenException;
import com.example.demo.domain.models.Role;
import com.example.demo.domain.models.User;
import com.example.demo.domain.service.RoleService;
import com.example.demo.domain.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(UserResource.USER)
public class UserResource {
    public static final String USER = "/user";
    public static final String LOGIN = "/login";
    public static final String TELEPHONE = "/{telephone}";
    public static final String ROLE = "/role";
    public final UserService userService;
    public final RoleService roleService;
    public final List<Role> allRole= Arrays.asList(Role.CLIENT,Role.ADMINISTRATOR,Role.ROOT);
    public final List<Role> adminRole=Arrays.asList(Role.ADMINISTRATOR,Role.ROOT);
    public final List<Role> rootRole= List.of(Role.ROOT);

    @Autowired
    public UserResource(UserService userService,RoleService roleService){
        this.userService = userService;
        this.roleService = roleService;
    }

    @SecurityRequirement(name = "basicAuth")
    @PreAuthorize("authenticated")
    @PostMapping(LOGIN)
    public TokenDto login(@Parameter(hidden = true)@AuthenticationPrincipal org.springframework.security.core.userdetails.User loginData){
         String token=this.userService.login(loginData.getUsername());
         return new TokenDto(token);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(TELEPHONE)
    public User read(@PathVariable String telephone){
            return this.userService.read(telephone);
    }

    @PreAuthorize("permitAll()")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody UserUploadDto userUpload){
        User newUser = this.userService.create(userUpload);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newUser.getTelephone())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping(TELEPHONE+ ROLE)
    public User updateRole(@PathVariable String telephone, @RequestBody String role){
        if(roleService.isCompetent(rootRole,this.extractRoleClaims())){
            return this.userService.updateAdminROOT(telephone,role);
        }else if(roleService.isCompetent(adminRole,this.extractRoleClaims())){
            return this.userService.updateAdminADMINISTRATOR(telephone,role);
        }else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public List<User> readAll(){
        if(roleService.isCompetent(adminRole,this.extractRoleClaims())){
            return this.userService.readAll();
        }else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT') or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping(TELEPHONE)
    public User update(@PathVariable String telephone, @RequestBody UserUpdateDto userUpdate){
        if(Role.isCompetent(adminRole,this.extractRoleClaims())||extractUserName().equals(telephone)){
            return this.userService.update(telephone,userUpdate);
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
