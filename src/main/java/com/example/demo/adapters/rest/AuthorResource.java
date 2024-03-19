package com.example.demo.adapters.rest;

import com.example.demo.adapters.rest.dto.AuthorUploadDto;
import com.example.demo.domain.exceptions.ConflictException;
import com.example.demo.domain.exceptions.ForbiddenException;
import com.example.demo.domain.models.Author;
import com.example.demo.domain.models.Role;
import com.example.demo.domain.service.AuthorService;
import com.example.demo.domain.service.FileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(AuthorResource.AUTHOR)
public class AuthorResource {
    public static final String AUTHOR = "/author";
    public static final String AUTHOR_ID = "/{authorId}";
    public static final String IMG = "/image";
    private final AuthorService authorService;
    private final FileService fileService;
    public final List<Role> adminRole=Arrays.asList(Role.ADMINISTRATOR,Role.ROOT);

    @Autowired
    public AuthorResource(AuthorService authorService
            ,FileService fileService){
        this.authorService = authorService;
        this.fileService = fileService;
    }

    //POST
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public Author create(@RequestBody AuthorUploadDto authorData){
        if(Role.isCompetent(adminRole,this.extractRoleClaims())){
            return this.authorService.create(authorData);
        }else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }

    //GET
    @GetMapping(AUTHOR_ID)
    public Author read(@PathVariable String authorId){
        return this.authorService.read(authorId);
    }

    @GetMapping
    public List<Author> readAll(){
        return this.authorService.readAll();
    }

    //PUT
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping(AUTHOR_ID)
    public Author update(@PathVariable String authorId,@RequestBody AuthorUploadDto authorData){
        if(Role.isCompetent(adminRole,this.extractRoleClaims())){
            return this.authorService.update(authorId,authorData);
        }else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }

    //DELETE
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping(AUTHOR_ID)
    public Author delete(@PathVariable String authorId) throws IOException {
        if(Role.isCompetent(adminRole,this.extractRoleClaims())){
            Author author = this.authorService.delete(authorId);
            if(!author.getImgFileName().equals("user.png")&&!author.getImgFileName().isEmpty()){
                String authorImagePackage = "src/main/resources/static/images/author/";
                fileService.fileDelete(authorImagePackage,author.getImgFileName());
            }
            return author;
        }else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')  or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping(value = AUTHOR_ID+IMG,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Author updateImage(@PathVariable String authorId, @RequestPart("file") MultipartFile file) throws IOException {
        if(Role.isCompetent(adminRole,this.extractRoleClaims())){
            return this.saveImage(authorId,file);
        }else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }

    private Author saveImage(String id,MultipartFile file) throws IOException {
        Author author = this.authorService.read(id);
        if (!file.isEmpty()) {
            this.fileService.fileSizeTooLarge(file,5 * 1024);
            this.fileService.isImageType(file);
            String authorImagePackage = "src/main/resources/static/images/author/";
            String fileName = this.fileService.fileWrite(authorImagePackage,"author",author.getAuthorId(),file);
            return this.authorService.uploadImg(id,"https://localhost/images/author/",fileName);
        }
        throw new ConflictException("update fault");
    }

    private Role extractRoleClaims() {
        List< String > roleClaims = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return Role.of(roleClaims.get(0));
    }
}
