package com.example.demo.domain.service;

import com.example.demo.TestConfig;
import com.example.demo.domain.exceptions.UnprocessableEntityException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
public class FileServiceTests {
    @Autowired
    private FileService fileService;
    @Test
    void testIsImageExtension(){
        assertEquals("jpg",fileService.isImageExtension("test.jpg"));
        assertEquals("jpg",fileService.isImageExtension("test.test.jpg"));
        assertEquals("JPG",fileService.isImageExtension("test.JPG"));
        assertThrows(UnprocessableEntityException.class,()->fileService.isImageExtension("test.mp3"));
        assertThrows(UnprocessableEntityException.class,()->fileService.isImageExtension(""));
    }
}
