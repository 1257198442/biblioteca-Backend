package com.example.demo.adapters.rest;

import com.example.demo.adapters.rest.dto.BookUploadDto;
import com.example.demo.adapters.rest.show.BookByShow;
import com.example.demo.domain.exceptions.ForbiddenException;
import com.example.demo.domain.models.Book;
import com.example.demo.domain.models.Role;
import com.example.demo.domain.service.BookService;
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
@RequestMapping(BookResource.BOOK)
public class BookResource {
    public static final String BOOK = "/book";
    public static final String BOOK_ID = "/{bookId}";
    public static final String All_LANGUAGE = "/all_language";
    private final BookService bookService;
    public final List<Role> adminRole= Arrays.asList(Role.ADMINISTRATOR,Role.ROOT);

    @Autowired
    public BookResource(BookService bookService){
        this.bookService = bookService;
    }

    //POST
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public Book create(@RequestBody BookUploadDto bookUploadDate){
        if(Role.isCompetent(adminRole,this.extractRoleClaims())){
            return this.bookService.create(bookUploadDate);
        }else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }

    //GET
    @GetMapping(BOOK_ID)
    public BookByShow readByBookId(@PathVariable String bookId){
        return this.bookService.readByBookId(bookId);
    }

    @GetMapping
    public List<BookByShow> readAll(){
        return this.bookService.readAll();
    }

    @GetMapping(All_LANGUAGE)
    public List<String> getAllBookLanguage(){
        return this.bookService.getAllBookLanguage();
    }

    private Role extractRoleClaims() {
        List< String > roleClaims = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return Role.of(roleClaims.get(0));
    }
}
