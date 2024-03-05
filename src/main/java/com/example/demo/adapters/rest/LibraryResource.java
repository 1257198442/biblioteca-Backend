package com.example.demo.adapters.rest;

import com.example.demo.adapters.rest.dto.LibraryUpdateDto;
import com.example.demo.domain.exceptions.ForbiddenException;
import com.example.demo.domain.models.Library;
import com.example.demo.domain.models.Role;
import com.example.demo.domain.service.RoleService;
import com.example.demo.domain.service.LibraryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


//@Rest
@RestController
@RequestMapping(LibraryResource.LIBRARY)
public class LibraryResource {
    public static final String LIBRARY = "/library";
    public static final String NAME = "/{name}";
    private final LibraryService libraryService;
    private final RoleService roleService;
    public final List<Role> rootRole= List.of(Role.ROOT);
    @Autowired
    public LibraryResource(LibraryService libraryService, RoleService roleService){
        this.libraryService = libraryService;
        this.roleService = roleService;
    }

    //PUT
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')  or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping()
    public Library update(@RequestBody LibraryUpdateDto libraryUpdate){
        if(roleService.isCompetent(rootRole,this.extractRoleClaims())){
            return this.libraryService.update(libraryUpdate,"BIBLIOTECA");
        }else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }

    //GET
    @GetMapping()
    public Library read(){
        return this.libraryService.read("BIBLIOTECA");
    }

    private Role extractRoleClaims() {
        List< String > roleClaims = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return Role.of(roleClaims.get(0));
    }
}