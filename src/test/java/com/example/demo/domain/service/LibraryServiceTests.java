package com.example.demo.domain.service;

import com.example.demo.TestConfig;
import com.example.demo.adapters.rest.dto.LibraryUpdateDto;
import com.example.demo.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
public class LibraryServiceTests {
    @Autowired
    private LibraryService libraryService;

    @Test
    void testRead(){
        assertNotNull(libraryService.read("BIBLIOTECA"));
        assertThrows(NotFoundException.class, () -> libraryService.read("null"));
    }

    @Test
    void testUpdate(){
        LibraryUpdateDto library1 = LibraryUpdateDto.builder()
                .address("X.º XX XXXXXXX, XX, 28001, XXXXXX, Madrid")
                .telephone("test")
                .email("bibliotecaMadrid@email.com")
                .postalCode("28001")
                .businessHours("Monday to Friday 10:00 to 22:00 Saturday and Sunday 10:00 to 16:00")
                .introduction("this is test introduction")
                .twitter("https://www.twitter.com/")
                .googleMail("https://mail.google.com/")
                .instagram("https://www.instagram.com/")
                .facebook("https://www.facebook.com/")
                .discord("https://www.discord.com/")
                .build();
        LibraryUpdateDto library2 = LibraryUpdateDto.builder()
                .address("X.º XX XXXXXXX, XX, 28001, XXXXXX, Madrid")
                .telephone("6669996669")
                .email("bibliotecaMadrid@email.com")
                .postalCode("28001")
                .businessHours("Monday to Friday 10:00 to 22:00 Saturday and Sunday 10:00 to 16:00")
                .introduction("this is test introduction")
                .twitter("https://www.twitter.com/")
                .googleMail("https://mail.google.com/")
                .instagram("https://www.instagram.com/")
                .facebook("https://www.facebook.com/")
                .discord("https://www.discord.com/")
                .build();
        String name = "BIBLIOTECA";
        assertNotNull(libraryService.update(library1,name));
        assertEquals(library1.getTelephone(),libraryService.read(name).getTelephone());
        assertNotNull(libraryService.update(library2,name));
    }
}
