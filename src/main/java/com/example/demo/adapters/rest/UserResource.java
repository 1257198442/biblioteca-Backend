package com.example.demo.adapters.rest;


import com.example.demo.adapters.rest.dto.*;
import com.example.demo.domain.exceptions.ForbiddenException;
import com.example.demo.domain.exceptions.UnauthorizedException;
import com.example.demo.domain.models.Role;
import com.example.demo.domain.models.Setting;
import com.example.demo.domain.models.User;
import com.example.demo.domain.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    public static final String SETTING = "/setting";
    public static final String TELEPHONE = "/{telephone}";
    public static final String PASSWORD = "/password";
    public static final String RESET_PASSWORD = "/resetPassword";
    public static final String ROLE = "/role";
    public final UserService userService;
    public final List<Role> allRole= Arrays.asList(Role.CLIENT,Role.ADMINISTRATOR,Role.ROOT);
    public final List<Role> adminRole=Arrays.asList(Role.ADMINISTRATOR,Role.ROOT);
    public final List<Role> rootRole= List.of(Role.ROOT);

    @Value("${Reset.password}")
    public   String resetPassword;
    @Autowired
    public UserResource(UserService userService){
        this.userService = userService;
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
        if(Role.isCompetent(rootRole,this.extractRoleClaims())){
            return this.userService.updateRoleROOT(telephone,role);
        }else if(Role.isCompetent(adminRole,this.extractRoleClaims())){
            return this.userService.updateRoleADMINISTRATOR(telephone,role);
        }else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public List<User> readAll(){
        if(Role.isCompetent(adminRole,this.extractRoleClaims())){
            return this.userService.readAll();
        }else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT') or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping(TELEPHONE)
    public User update(@PathVariable String telephone, @RequestBody UserUpdateDto userUpdate){
        if(hasPermission(rootRole,telephone)){
            return this.userService.update(telephone,userUpdate);
        } else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT') or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping(TELEPHONE+SETTING)
    public User updateSetting(@PathVariable String telephone, @RequestBody Setting setting){
        if(hasPermission(rootRole,telephone)){
            return this.userService.updateSetting(telephone,setting);
        } else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping(TELEPHONE+PASSWORD)
    public User changePassword(@PathVariable String telephone,@RequestBody PasswordUpdateDto password){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(hasPermission(rootRole,telephone)){
            if(encoder.matches(password.getOldPassword(),this.userService.getUserPassword(telephone))){
                return this.userService.changePassword(telephone,password.getNewPassword());
            }else {
                throw new UnauthorizedException("The password is wrong.");
            }
        }else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping(TELEPHONE+RESET_PASSWORD)
    public User resetPassword(@PathVariable String telephone){
        if (Role.isCompetent(rootRole,this.extractRoleClaims())){
            return this.userService.changePassword(telephone,this.resetPassword);
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