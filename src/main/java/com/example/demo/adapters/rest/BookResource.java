package com.example.demo.adapters.rest;

import com.example.demo.adapters.rest.dto.BookUpdateDto;
import com.example.demo.adapters.rest.dto.BookUploadDto;
import com.example.demo.adapters.rest.show.BookByShow;
import com.example.demo.domain.exceptions.ConflictException;
import com.example.demo.domain.exceptions.ForbiddenException;
import com.example.demo.domain.models.Author;
import com.example.demo.domain.models.Book;
import com.example.demo.domain.models.Role;
import com.example.demo.domain.service.BookService;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(BookResource.BOOK)
public class BookResource {
    public static final String BOOK = "/book";
    public static final String BOOK_ID = "/{bookId}";
    public static final String All_LANGUAGE = "/all_language";
    public static final String STATUS = "/status";
    public static final String IMG = "/image";
    public static final String RANDOM = "/random";
    public static final String SEARCH = "/search";
    private final BookService bookService;
    private final FileService fileService;
    public final List<Role> adminRole= Arrays.asList(Role.ADMINISTRATOR,Role.ROOT);
    public final List<Role> rootRole= List.of(Role.ROOT);

    @Autowired
    public BookResource(BookService bookService,
                        FileService fileService){
        this.bookService = bookService;
        this.fileService = fileService;
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
        return this.bookService.getBookShow(bookId);
    }

    @GetMapping
    public List<BookByShow> readAll(){
        return this.bookService.readAll();
    }

    @GetMapping(All_LANGUAGE)
    public List<String> getAllBookLanguage(){
        return this.bookService.getAllBookLanguage();
    }

    //PUT
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping(BOOK_ID)
    public BookByShow Update(@RequestBody BookUpdateDto bookDate, @PathVariable String bookId){
        if(Role.isCompetent(adminRole,this.extractRoleClaims())){
            return this.bookService.update(bookDate,bookId);
        }else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping(BOOK_ID+STATUS)
    public Book modifyBookStatus(@PathVariable String bookId,@RequestBody String status){
        if(Role.isCompetent(rootRole,this.extractRoleClaims())){
            return this.bookService.modifyBookStatusRoot(bookId, status);
        } else if(Role.isCompetent(adminRole,this.extractRoleClaims())){
            return this.bookService.modifyBookStatusAdmin(bookId, status);
        }else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping(value = BOOK_ID+IMG,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Book upLoadBookImage(@PathVariable String bookId,@RequestPart("file") MultipartFile file) throws IOException {
        if(Role.isCompetent(adminRole,this.extractRoleClaims())){
            return this.saveImage(bookId,file);
        }else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }

    private Book saveImage(String id, MultipartFile file) throws IOException {
        Book book = this.bookService.read(id);
        if (!file.isEmpty()) {
            this.fileService.fileSizeTooLarge(file,5 * 1024);
            this.fileService.isImageType(file);
            String authorImagePackage = "src/main/resources/static/images/book/";
            String fileName = this.fileService.fileWrite(authorImagePackage,"book",book.getBookID(),file);
            return this.bookService.uploadImg(id,"https://localhost/images/book/",fileName);
        }
        throw new ConflictException("update fault");
    }

    //DELETE
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping(BOOK_ID)
    public Book delete(@PathVariable String bookId) throws IOException {
        if(Role.isCompetent(adminRole,this.extractRoleClaims())){
            Book book = this.bookService.delete(bookId);
            if(!book.getImgFileName().equals("default.jpg")&&!book.getImgFileName().isEmpty()){
                String bookImagePackage = "src/main/resources/static/images/book/";
                fileService.fileDelete(bookImagePackage,book.getImgFileName());
            }
            return book;
        }else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }

    }

    @GetMapping(RANDOM)
    public BookByShow randomBook(){
        return this.bookService.randomBook();
    }

    @GetMapping(SEARCH)
    public List<BookByShow> readByNameAndPublisherAndAuthor(@RequestParam(required = false) String name
            , @RequestParam(required = false)String publisher
            , @RequestParam(required = false)String authorName
            , @RequestParam(required = false)String language
            , @RequestParam(required = false)String type
            , @RequestParam(required = false)String authorId
            , @RequestParam(required = false)String barcode
            , @RequestParam(required = false)String issn
            , @RequestParam(required = false)String isbn
    ){
        if(authorId==null){
            return this.bookService.searchBook(name,publisher,authorName,language,type,barcode,issn,isbn);
        }else {
            return this.bookService.readAllByAuthorId(authorId);
        }

    }

    private Role extractRoleClaims() {
        List< String > roleClaims = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return Role.of(roleClaims.get(0));
    }
}
