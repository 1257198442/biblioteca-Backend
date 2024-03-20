package com.example.demo.adapters.rest;

import com.example.demo.domain.exceptions.ForbiddenException;
import com.example.demo.domain.models.Role;
import com.example.demo.domain.models.Type;
import com.example.demo.domain.service.TypeService;
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
@RequestMapping(TypeResource.TYPE)
public class TypeResource {
    public static final String TYPE = "/type";
    public static final String NAME = "/{name}";
    private final TypeService typeService;
    public final List<Role> adminRole= Arrays.asList(Role.ADMINISTRATOR,Role.ROOT);
    @Autowired
    public TypeResource(TypeService typeService){
        this.typeService = typeService;
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')  or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public Type create(@RequestBody Type type){
        if(Role.isCompetent(adminRole,this.extractRoleClaims())){
            return this.typeService.create(type);
        }else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }

    @GetMapping(NAME)
    public Type read(@PathVariable String name){
        return this.typeService.read(name);
    }

    @GetMapping
    public List<Type> readAll(){
        return this.typeService.readAll();
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')  or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping(NAME)
    public Type delete(@PathVariable String name){
        if(Role.isCompetent(adminRole,this.extractRoleClaims())){
        return this.typeService.delete(name);
        }else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')  or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping(NAME)
    public Type update(@PathVariable String name,@RequestBody String description){
        if(Role.isCompetent(adminRole,this.extractRoleClaims())){
            return this.typeService.update(name,description);
        }else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }

    }

    private Role extractRoleClaims() {
        List< String > roleClaims = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return Role.of(roleClaims.get(0));
    }
}
