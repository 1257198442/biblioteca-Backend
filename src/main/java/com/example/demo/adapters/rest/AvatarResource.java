package com.example.demo.adapters.rest;

import com.example.demo.domain.exceptions.ForbiddenException;
import com.example.demo.domain.exceptions.UnprocessableEntityException;
import com.example.demo.domain.models.Avatar;
import com.example.demo.domain.models.Role;
import com.example.demo.domain.service.AvatarService;
import com.example.demo.domain.service.RoleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

//@Rest
@RestController
@RequestMapping(AvatarResource.AVATAR)
public class AvatarResource {
    public static final String AVATAR = "/avatar";
    public static final String TELEPHONE = "/{telephone}";
    private final AvatarService avatarService;
    private final RoleService adminService;
    public final List<Role> rootRole= Arrays.asList(Role.ROOT);

    @Autowired
    public AvatarResource(AvatarService avatarService, RoleService roleService){
        this.avatarService = avatarService;
        this.adminService = roleService;
    }

    //PUT
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping(value = TELEPHONE,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Avatar updateAvatarByMobile(@PathVariable String telephone, @RequestPart("file") MultipartFile file){
        if(hasPermission(rootRole,telephone)){
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
        Avatar avatar = this.avatarService.read(telephone);
        if (!file.isEmpty()) {
            try {
                String fileName = "avatar"+ Base64.getEncoder().encodeToString(avatar.getTelephone().getBytes(StandardCharsets.UTF_8))+"."+getExtension(file);
                Path path = Paths.get("src/main/resources/static/images/userUpload/" + fileName);
                System.out.println(fileName);
                Files.write(path, file.getBytes());
                Avatar avatar1 = new Avatar(fileName,"https://localhost/images/userUpload/"+ fileName,telephone,LocalDateTime.now());
                return this.avatarService.update(avatar1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        throw new UnprocessableEntityException("update fault");
    }

    private String getExtension(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new UnprocessableEntityException("File is empty or null");
        }
        String fileName = file.getOriginalFilename();
        int lastDotIndex;
        if (fileName != null) {
            lastDotIndex = fileName.lastIndexOf('.');
        }else {
            throw new UnprocessableEntityException("update fault");
        }
        String extension = lastDotIndex != -1 ? fileName.substring(lastDotIndex + 1) : "";
        if(isImageExtension(extension)){
            return extension;
        }else {
            throw new UnprocessableEntityException("update fault");
        }
    }

    private static final Set<String> COMMON_IMAGE_EXTENSIONS = new HashSet<>(Arrays.asList(
            "jpeg", "jpg", "png", "gif", "bmp", "dib", "tiff", "tif", "webp", "svg", "ico"
    ));

    public static boolean isImageExtension(String extension) {
        // 检查传入的扩展名是否在常用图片后缀集合中
        return COMMON_IMAGE_EXTENSIONS.contains(extension);
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