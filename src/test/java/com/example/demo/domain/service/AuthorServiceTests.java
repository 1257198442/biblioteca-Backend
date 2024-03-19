package com.example.demo.domain.service;

import com.example.demo.TestConfig;
import com.example.demo.adapters.rest.dto.AuthorUploadDto;
import com.example.demo.domain.exceptions.NotFoundException;
import com.example.demo.domain.models.Author;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@TestConfig
public class AuthorServiceTests {
    @Autowired
    private AuthorService authorService;

    @Test
    void testCreateAndRead(){
        AuthorUploadDto authorUploadDto = AuthorUploadDto.builder().nationality("test").name("testCreate").description("test text").build();
        String id = authorService.create(authorUploadDto).getAuthorId();
        assertEquals(12,id.length());
        Author author = authorService.read(id);
        assertEquals(authorUploadDto.getNationality(),author.getNationality());
        assertEquals(authorUploadDto.getName(),author.getName());
        assertEquals(authorUploadDto.getDescription(),author.getDescription());
        assertEquals("https://localhost/images/author/user.png",author.getImgUrl());
        assertThrows(NotFoundException.class,()->authorService.read("null"));
    }

    @Test
    void testReadAll(){
        assertNotNull(authorService.readAll());
    }

    @Test
    void testGetAuthorData(){
        assertNotNull(authorService.getAuthorData("111"));
        assertThat(authorService.getAuthorData("null")).isNull();
    }
}
