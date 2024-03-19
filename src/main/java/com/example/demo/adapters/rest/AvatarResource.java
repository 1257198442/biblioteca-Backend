package com.example.demo.adapters.rest;

import com.example.demo.domain.exceptions.ForbiddenException;
import com.example.demo.domain.exceptions.MaxUploadSizeExceededException;
import com.example.demo.domain.exceptions.UnprocessableEntityException;
import com.example.demo.domain.models.Avatar;
import com.example.demo.domain.models.Role;
import com.example.demo.domain.service.AvatarService;
import com.example.demo.domain.service.FileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(AvatarResource.AVATAR)
public class AvatarResource {
    public static final String AVATAR = "/avatar";
    public static final String TELEPHONE = "/{telephone}";
    private final AvatarService avatarService;
    private final FileService fileService;
    public final List<Role> rootRole= Arrays.asList(Role.ROOT);

    @Autowired
    public AvatarResource(AvatarService avatarService
            ,FileService fileService){
        this.avatarService = avatarService;
        this.fileService = fileService;
    }

    //PUT
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping(value = TELEPHONE,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Avatar updateAvatarByMobile(@PathVariable String telephone, @RequestPart("file") MultipartFile file) throws IOException {
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

    public Avatar saveAvatar(String telephone, MultipartFile file) throws IOException {
        Avatar avatar = this.avatarService.read(telephone);
        if (!file.isEmpty()) {
            this.fileService.fileSizeTooLarge(file,5 * 1024);
            this.fileService.isImageType(file);
            String avatarPackage="src/main/resources/static/images/avatar/userUpload/";
            String fileName = fileService.fileWrite(avatarPackage,"avatar",avatar.getTelephone(),file);
            avatar.setFileName(fileName);
            avatar.setUrl("https://localhost/images/userUpload/"+ fileName);
            avatar.setFileName(fileName);
            avatar.setUploadTime(LocalDateTime.now());
            return this.avatarService.update(avatar);
        }
        throw new UnprocessableEntityException("update fault");
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