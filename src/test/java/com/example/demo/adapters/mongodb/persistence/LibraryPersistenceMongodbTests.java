package com.example.demo.adapters.mongodb.persistence;

import com.example.demo.TestConfig;
import com.example.demo.domain.models.Library;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestConfig
public class LibraryPersistenceMongodbTests {
    @Autowired
    private LibraryPersistenceMongodb libraryPersistenceMongodb;

    @Test
    void testRead(){
        Library library = libraryPersistenceMongodb.read("BIBLIOTECA");
        assertEquals("X.º XX XXXXXXX, XX, 28001, XXXXXX, Madrid",library.getAddress());
        assertEquals("6669996669",library.getTelephone());
        assertEquals("BIBLIOTECA", library.getName());
        assertEquals("bibliotecaMadrid@email.com", library.getEmail());
        assertEquals("28001", library.getPostalCode());
        assertEquals("Monday to Friday 10:00 to 22:00 Saturday and Sunday 10:00 to 16:00", library.getBusinessHours());
        assertEquals("this is test introduction", library.getIntroduction());
        assertEquals("https://www.twitter.com/", library.getTwitter());
        assertEquals("https://mail.google.com/", library.getGoogleMail());
        assertEquals("https://www.instagram.com/", library.getInstagram());
        assertEquals("https://www.facebook.com/", library.getFacebook());
        assertEquals("https://www.discord.com/", library.getDiscord());
    }
    @Test
    void testUpdate(){
        Library library = Library.builder()
                .address("X.º XX XXXXXXX, XX, 28001, XXXXXX, Madrid")
                .telephone("test")
                .name("BIBLIOTECA")
                .email("bibliotecaMadrid@email.com")
                .postalCode("28001")
                .businessHours("Monday to Friday 10:00 to 22:00 Saturday and Sunday 10:00 to 16:00")
                .introduction("this is test introduction")
                .twitter("https://www.twitter.com/")
                .googleMail("https://mail.google.com/")
                .instagram("https://www.instagram.com/")
                .facebook("https://www.facebook.com/")
                .discord("https://www.discord.com/").build();
        assertEquals(library,libraryPersistenceMongodb.update(library));
        library.setTelephone("6669996669");
        assertEquals(library,libraryPersistenceMongodb.update(library));
    }

}
