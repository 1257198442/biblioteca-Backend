package com.example.demo.domain.service;

import com.example.demo.adapters.rest.dto.BookUploadDto;
import com.example.demo.adapters.rest.show.BookByShow;
import com.example.demo.domain.exceptions.UnprocessableEntityException;
import com.example.demo.domain.models.Book;
import com.example.demo.domain.models.BookStatus;
import com.example.demo.domain.models.Language;
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

    @Autowired
    public BookService(BookPersistence bookPersistence,RandomStringService randomStringService){
        this.bookPersistence = bookPersistence;
        this.randomStringService = randomStringService;
    }

    public Book create(BookUploadDto bookUploadDate){
        Book book = new Book();
        BeanUtils.copyProperties(bookUploadDate,book);
        book.setBookID(randomStringService.generateRandomString(12));
        book.setEntryTime(LocalDateTime.now());
        book.setImgUrl("https://localhost/images/book/default.jpg");
        book.setStatus(BookStatus.DISABLE);
        book.setLanguage(Language.fromString(bookUploadDate.getLanguage()));
        return this.bookPersistence.create(book);
    }

    public BookByShow readByBookId(String bookId){
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
        return bookReturnData;
    }

    public Stream<BookByShow> convertToBookReturnList(List<Book> books) {
        return books.stream()
                .map(Book::toShowOmit)
                .map(this::bookToBookShow);
    }

}
