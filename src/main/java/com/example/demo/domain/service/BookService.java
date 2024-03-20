package com.example.demo.domain.service;

import com.example.demo.adapters.rest.dto.BookUpdateDto;
import com.example.demo.adapters.rest.dto.BookUploadDto;
import com.example.demo.adapters.rest.show.BookByShow;
import com.example.demo.domain.exceptions.ForbiddenException;
import com.example.demo.domain.exceptions.LockedResourceException;
import com.example.demo.domain.models.*;
import com.example.demo.domain.persistence.BookPersistence;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BookService {
    private final BookPersistence bookPersistence;
    private final RandomStringService randomStringService;
    private final AuthorService authorService;
    private final TypeService typeService;

    @Autowired
    public BookService(BookPersistence bookPersistence,
                       RandomStringService randomStringService,
                       AuthorService authorService,
                       TypeService typeService){
        this.bookPersistence = bookPersistence;
        this.randomStringService = randomStringService;
        this.authorService = authorService;
        this.typeService = typeService;
    }

    public Book create(BookUploadDto bookUploadDate){
        Book book = new Book();
        BeanUtils.copyProperties(bookUploadDate,book);
        book.setBookID(randomStringService.generateRandomString(12));
        book.setEntryTime(LocalDateTime.now());
        book.setImgUrl("https://localhost/images/book/default.jpg");
        book.setImgFileName("default.jpg");
        book.setStatus(BookStatus.DISABLE);
        book.setLanguage(Language.fromString(bookUploadDate.getLanguage()));
        book.setAuthorId(bookUploadDate.getAuthorId()==null?null:this.getAuthorID(bookUploadDate.getAuthorId()));
        book.setBookType(bookUploadDate.getBookType()==null?null:this.getTypeID(bookUploadDate.getBookType()));
        return this.bookPersistence.create(book);
    }
    public Book read(String id){
        return this.bookPersistence.read(id);
    }
    public BookByShow getBookShow(String bookId){
       return this.bookToBookShow(this.bookPersistence.read(bookId)) ;
    }

    public List<BookByShow> readAll(){
        return this.convertToBookReturnList(this.bookPersistence.readAll()).collect(Collectors.toList());
    }

    public List<String> getAllBookLanguage(){
        return Language.getAllLanguages();
    }

    public BookByShow bookToBookShow(Book book){
        BookByShow bookReturnData = new BookByShow();
        BeanUtils.copyProperties(book,bookReturnData);
        if(book.getAuthorId() != null){
            List<Author> authors = book.getAuthorId().stream()
                    .map(this.authorService::getAuthorData)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            bookReturnData.setAuthor(authors);
        }
        if(book.getBookType()!=null){
            List<Type> types = book.getBookType().stream()
                    .map(this.typeService::getType)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            bookReturnData.setBookType(types);
        }
        bookReturnData.setBorrowCount(0);
        return bookReturnData;
    }

    public Stream<BookByShow> convertToBookReturnList(List<Book> books) {
        return books.stream()
                .map(Book::toShowOmit)
                .map(this::bookToBookShow);
    }

    public List<String> getAuthorID(List<String> authors){
        return authors.stream()
                .map(this.authorService::getAuthorData)
                .filter(Objects::nonNull)
                .map(Author::getAuthorId)
                .collect(Collectors.toList());
    }
    public List<String> getTypeID(List<String> types){
        return types.stream()
                .map(this.typeService::getType)
                .filter(Objects::nonNull)
                .map(Type::getName)
                .collect(Collectors.toList());
    }

    public BookByShow update(BookUpdateDto bookDate, String bookId){
        Book book = this.bookPersistence.read(bookId);
        BeanUtils.copyProperties(bookDate,book);
        book.setLanguage(Language.fromString(bookDate.getLanguage()));
        if(bookDate.getAuthorId()==null){
            book.setAuthorId(null);
        }else {
            book.setAuthorId(this.getAuthorID(bookDate.getAuthorId()));
        }
        if(bookDate.getBookType()==null){
            book.setBookType(null);
        }else {
            book.setBookType(this.getTypeID(bookDate.getBookType()));
        }
        return this.bookToBookShow(this.bookPersistence.update(book));
    }

    public Book modifyBookStatusAdmin(String bookId,String bookStatus){
        Book book = this.bookPersistence.read(bookId);
        if(book.getStatus()!=BookStatus.OCCUPIED){
            book.setStatus(BookStatus.fromString(bookStatus));
            return this.bookPersistence.update(book);
        }else {
            throw new LockedResourceException("Book "+bookId+" is occupied You are not authorised to modify its status, this operation can only be performed using the root account.");
        }
    }

    public Book modifyBookStatusRoot(String bookId,String bookStatus){
        Book book = this.bookPersistence.read(bookId);
        book.setStatus(BookStatus.fromString(bookStatus));
        return this.bookPersistence.update(book);
    }

    public Book delete(String bookId){
        return this.bookPersistence.delete(bookId);
    }

    public Book uploadImg(String id,String url,String fileName){
        Book book = this.bookPersistence.read(id);
        book.setImgUrl(url+fileName);
        book.setImgFileName(fileName);
        return this.bookPersistence.update(book);
    }
}
