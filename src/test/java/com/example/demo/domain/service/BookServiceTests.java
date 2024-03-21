package com.example.demo.domain.service;

import com.example.demo.TestConfig;
import com.example.demo.adapters.rest.dto.BookUpdateDto;
import com.example.demo.adapters.rest.dto.BookUploadDto;
import com.example.demo.adapters.rest.show.BookByShow;
import com.example.demo.domain.exceptions.NotFoundException;
import com.example.demo.domain.models.Book;
import com.example.demo.domain.models.BookStatus;
import com.example.demo.domain.models.Language;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestConfig
public class BookServiceTests {

    @Autowired
    private BookService bookService;
    @Test
    void testReadAndCreateAndDelete(){
        BookUploadDto bookUploadDto = BookUploadDto.builder()
                .name("testCreate")
                .description("this is a test for create book")
                .publisher("test publisher")
                .barcode("11223344")
                .language("English")
                .isbn("abcd1234")
                .deposit(new BigDecimal("99"))
                .authorId(List.of())
                .bookType(List.of())
                .language("English").build();

        String id = bookService.create(bookUploadDto).getBookID();
        assertEquals(12,id.length());
        BookByShow book = bookService.getBookShow(id);
        assertEquals(bookUploadDto.getName(),book.getName());
        assertEquals(bookUploadDto.getDescription(),book.getDescription());
        assertEquals(bookUploadDto.getPublisher(),book.getPublisher());
        assertEquals(bookUploadDto.getBarcode(),book.getBarcode());
        assertEquals(bookUploadDto.getIsbn(),book.getIsbn());
        assertEquals(bookUploadDto.getDeposit(),book.getDeposit());
        assertEquals(Language.English,book.getLanguage());
        assertEquals(BookStatus.DISABLE,book.getStatus());
        assertTrue(book.getAuthor().isEmpty());
        assertTrue(book.getBookType().isEmpty());
        assertNotNull(book.getEntryTime());
        assertNotNull(book.getImgUrl());
        assertThrows(NotFoundException.class,()->bookService.getBookShow("null"));
        bookService.delete(id);
        assertThrows(NotFoundException.class,()->bookService.getBookShow(id));
    }

    @Test
    void testUpdate(){
        BookUpdateDto bookUpdateDto = BookUpdateDto.builder()
                .name("testUpdate")
                .description("description1")
                .publisher("publisher1")
                .barcode("11223344")
                .language("English")
                .isbn("abcd1234")
                .deposit(new BigDecimal("77"))
                .authorId(List.of())
                .bookType(List.of())
                .build();
        bookService.update(bookUpdateDto,"4");
        Book book = bookService.read("4");
        assertEquals("testUpdate",book.getName());
        assertEquals("description1",book.getDescription());
        assertEquals("publisher1",book.getPublisher());
        assertEquals("11223344",book.getBarcode());
        assertEquals("abcd1234",book.getIsbn());
        assertEquals(new BigDecimal("77"),book.getDeposit());
    }

    @Test
    void testRandomBook(){
        assertNotNull(bookService.randomBook());
    }

    @Test
    void testSearch(){
        assertNotNull(bookService.searchBook(null,null,null,null,null,null,null,null));
        assertEquals(1,bookService.searchBook("1",null,null,null,null,null,null,null).size());
        assertEquals(7,bookService.searchBook(null,"test",null,null,null,null,null,null).size());
        assertEquals(5,bookService.searchBook(null,null,"author3",null,null,null,null,null).size());
        assertEquals(9,bookService.searchBook(null,null,null,"English",null,null,null,null).size());
        assertEquals(4,bookService.searchBook(null,null,null,null,"test5",null,null,null).size());
        assertEquals(1,bookService.searchBook(null,null,null,null,null,"abc",null,null).size());
        assertEquals(1,bookService.searchBook(null,null,null,null,null,null,"1111111111",null).size());
        assertEquals(1,bookService.searchBook(null,null,null,null,null,null,null,"98989777979").size());
    }

    @Test
    void testReadAllByAuthorId(){
        assertEquals(4,bookService.readAllByAuthorId("111").size());
        assertEquals(5,bookService.readAllByAuthorId("333").size());
    }


    @Test
    void testReadAll(){
        assertNotNull(bookService.readAll());
    }

}
