package com.example.demo.domain.service;

import com.example.demo.TestConfig;
import com.example.demo.domain.exceptions.UnprocessableEntityException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
        assertThrows(UnprocessableEntityException.class,()->fileService.isImageExtension(null));
    }

    @Test
    void testGetExtension(){
        assertThrows(UnprocessableEntityException.class,()->fileService.getExtension(null));
    }

    @Test
    void testFileDelete() throws IOException {
        fileService.fileWrite("src/main/resources/static/testImages/","test","123",getMultipartFile());
        fileService.fileDelete("src/main/resources/static/testImages/","test123.png");
    }

    private MultipartFile getMultipartFile() throws IOException {
        Path path = Path.of("src/main/resources/static/testImages/img.png");
        String name = "file";
        String originalFileName = "img.png";
        String contentType = "image/png";
        byte[] content = Files.readAllBytes(path);
        return new MockMultipartFile(name, originalFileName, contentType, content);
    }

}
