package com.example.demo.adapters.rest;

import com.example.demo.domain.exceptions.ForbiddenException;
import com.example.demo.domain.models.Book;
import com.example.demo.domain.models.CollectionList;
import com.example.demo.domain.models.Role;
import com.example.demo.domain.service.CollectionListService;
import com.example.demo.domain.service.UserService;
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
@RequestMapping(CollectionListResource.COLLECTION_LIST)
public class CollectionListResource {
    public static final String COLLECTION_LIST = "/collection_list";
    public static final String BOOK = "/book";
    public static final String TELEPHONE = "/{telephone}";
    public static final String ADD_BOOK = "/add_book";
    public static final String REMOVE_BOOK = "/remove_book";
    public final List<Role> adminRole= Arrays.asList(Role.ROOT,Role.ADMINISTRATOR);
    private final CollectionListService collectionListService;
    private final UserService userService;

    @Autowired
    public CollectionListResource(CollectionListService collectionListService,UserService userService){
        this.collectionListService = collectionListService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')  or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(TELEPHONE+BOOK)
    public List<Book> readBookData(@PathVariable String telephone){
        List<Book> bookList = this.collectionListService.readBookData(telephone);
        if(!userService.read(telephone).getSetting().getHideMyCollectionList()){
            return bookList;
        }
        if(hasPermission(adminRole,telephone)){
            return bookList;
        }else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')  or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(TELEPHONE)
    public CollectionList read(@PathVariable String telephone){
        CollectionList collectionList = collectionListService.read(telephone);
        if(!userService.read(telephone).getSetting().getHideMyCollectionList()){
            return collectionList;
        }
        if(hasPermission(adminRole,telephone)){
            return collectionList;
        }else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')  or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping( TELEPHONE+ADD_BOOK)
    public CollectionList addBook(@PathVariable String telephone, @RequestBody String bookId){
        if(hasPermission(adminRole,telephone)){
            return this.collectionListService.addBook(telephone,bookId);
        }else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')  or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping( TELEPHONE+REMOVE_BOOK)
    public CollectionList removeBook(@PathVariable String telephone,@RequestBody String bookId){
        if(hasPermission(adminRole,telephone)){
            return this.collectionListService.removeBook(telephone,bookId);
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
