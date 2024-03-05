package com.example.demo.adapters.rest;

import com.example.demo.domain.exceptions.ConflictException;
import com.example.demo.domain.exceptions.ForbiddenException;
import com.example.demo.domain.models.Avatar;
import com.example.demo.domain.models.Role;
import com.example.demo.domain.service.AvatarService;
import com.example.demo.domain.service.RoleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//@Rest
@RestController
@RequestMapping(AvatarResource.AVATAR)
public class AvatarResource {
    public static final String AVATAR = "/avatar";
    public static final String TELEPHONE = "/{telephone}";
    private final AvatarService avatarService;
    private final RoleService adminService;
    public final List<Role> adminRole= Arrays.asList(Role.ADMINISTRATOR,Role.ROOT);

    @Autowired
    public AvatarResource(AvatarService avatarService, RoleService roleService){
        this.avatarService = avatarService;
        this.adminService = roleService;
    }

    //PUT
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping(TELEPHONE)
    public Avatar updateAvatarByMobile(@PathVariable String telephone, @RequestPart("file") MultipartFile file){
        if(hasPermission(adminRole,telephone)){
            return this.saveAvatar(telephone,file);
        }else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }

    //GET
    @GetMapping(TELEPHONE)
    public Avatar read(@PathVariable String telephone){
        return this.avatarService.read(telephone);
    }

    public Avatar saveAvatar(String telephone, MultipartFile file){
        if (!file.isEmpty()) {
            try {
                String fileName = file.getOriginalFilename();
                Path path = Paths.get("src/main/resources/static/images/userUpload/" + fileName);
                Files.write(path, file.getBytes());
                Avatar avatar = new Avatar(fileName,"https://localhost/images/userUpload/"+ fileName,telephone,LocalDateTime.now());
                return this.avatarService.update(avatar);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        throw new ConflictException("update fault");
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