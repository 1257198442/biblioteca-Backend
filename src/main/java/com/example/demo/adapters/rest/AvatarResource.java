package com.example.demo.adapters.rest;

import com.example.demo.domain.exceptions.ForbiddenException;
import com.example.demo.domain.exceptions.MaxUploadSizeExceededException;
import com.example.demo.domain.exceptions.UnprocessableEntityException;
import com.example.demo.domain.models.Avatar;
import com.example.demo.domain.models.Role;
import com.example.demo.domain.service.AvatarService;
import com.example.demo.domain.service.RoleService;
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
    public final List<Role> rootRole= Arrays.asList(Role.ROOT);

    @Autowired
    public AvatarResource(AvatarService avatarService){
        this.avatarService = avatarService;
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
            if (this.fileSizeTooLarge(file)){
                throw new MaxUploadSizeExceededException("Avatar files size cannot be larger than 5MB");
            }
            Tika tika = new Tika();
            String fileContentType = tika.detect(file.getInputStream(), file.getOriginalFilename());
            if (!isImageType(fileContentType)) {
                throw new UnprocessableEntityException("File is not an image.");
            }
            try {
                String fileName = "avatar"+ avatar.getTelephone()+"."+getExtension(file);
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
            throw new UnprocessableEntityException("File is not an image.");
        }
        String extension = lastDotIndex != -1 ? fileName.substring(lastDotIndex + 1) : "";
        if(isImageExtension(extension)){
            return extension;
        }else {
            throw new UnprocessableEntityException("File is not an image.");
        }
    }

    private boolean isImageType(String contentType) {
        String[] imageTypes = {"image/jpeg", "image/jpg", "image/png", "image/gif",
                "image/bmp", "image/tiff", "image/webp", "image/svg+xml",
                "image/x-icon"};
        return Arrays.asList(imageTypes).contains(contentType);
    }

    private static final Set<String> COMMON_IMAGE_EXTENSIONS = new HashSet<>(Arrays.asList(
            "jpeg", "jpg", "png", "gif", "bmp", "tiff", "webp", "svg","ico"
    ));

    public static boolean isImageExtension(String extension) {
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

    private boolean fileSizeTooLarge (MultipartFile file){
        long maxFileSize = 5 * 1024 * 1024;
        long fileSize = file.getSize();
        return fileSize > maxFileSize;
    }
}