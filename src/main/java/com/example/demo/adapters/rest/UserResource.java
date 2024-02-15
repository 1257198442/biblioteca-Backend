package com.example.demo.adapters.rest;


import com.example.demo.adapters.rest.Dto.TokenDto;
import com.example.demo.domain.models.User;
import com.example.demo.domain.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UserResource.USER)
public class UserResource {
    public static final String USER = "/user";
    public static final String LOGIN = "/login";
    public static final String TELEPHONE = "/{telephone}";
    public final UserService userService;

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

}
