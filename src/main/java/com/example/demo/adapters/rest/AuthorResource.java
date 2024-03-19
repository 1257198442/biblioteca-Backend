package com.example.demo.adapters.rest;

import com.example.demo.adapters.rest.dto.AuthorUploadDto;
import com.example.demo.domain.exceptions.ForbiddenException;
import com.example.demo.domain.models.Author;
import com.example.demo.domain.models.Role;
import com.example.demo.domain.service.AuthorService;
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
@RequestMapping(AuthorResource.AUTHOR)
public class AuthorResource {
    public static final String AUTHOR = "/author";
    public static final String AUTHOR_ID = "/{authorId}";
    public static final String IMG = "/image";
    private final AuthorService authorService;
    public final List<Role> adminRole=Arrays.asList(Role.ADMINISTRATOR,Role.ROOT);

    @Autowired
    public AuthorResource(AuthorService authorService){
        this.authorService = authorService;
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

    private Role extractRoleClaims() {
        List< String > roleClaims = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return Role.of(roleClaims.get(0));
    }
}
