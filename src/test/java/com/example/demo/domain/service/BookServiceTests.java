package com.example.demo.domain.service;

import com.example.demo.TestConfig;
import com.example.demo.adapters.rest.dto.BookUploadDto;
import com.example.demo.adapters.rest.show.BookByShow;
import com.example.demo.domain.exceptions.NotFoundException;
import com.example.demo.domain.models.BookStatus;
import com.example.demo.domain.models.Language;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@TestConfig
public class BookServiceTests {

    @Autowired
    private BookService bookService;
    @Test
    void testReadAndCreate(){
        BookUploadDto bookUploadDto = BookUploadDto.builder()
                .name("testCreate")
                .description("this is a test for create book")
                .publisher("test publisher")
                .barcode("11223344")
                .isbn("abcd1234")
                .deposit(new BigDecimal("99"))
                .authorId(List.of())
                .language("English").build();

        String id = bookService.create(bookUploadDto).getBookID();
        assertEquals(12,id.length());
        BookByShow book = bookService.readByBookId(id);
        assertEquals(bookUploadDto.getName(),book.getName());
        assertEquals(bookUploadDto.getDescription(),book.getDescription());
        assertEquals(bookUploadDto.getPublisher(),book.getPublisher());
        assertEquals(bookUploadDto.getBarcode(),book.getBarcode());
        assertEquals(bookUploadDto.getIsbn(),book.getIsbn());
        assertEquals(bookUploadDto.getDeposit(),book.getDeposit());
        assertEquals(Language.English,book.getLanguage());
        assertEquals(BookStatus.DISABLE,book.getStatus());
        assertTrue(book.getAuthor().isEmpty());
        assertNotNull(book.getEntryTime());
        assertNotNull(book.getImgUrl());
        assertThrows(NotFoundException.class,()->bookService.readByBookId("null"));
    }

    @Test
    void testReadAll(){
        assertNotNull(bookService.readAll());
    }
}
