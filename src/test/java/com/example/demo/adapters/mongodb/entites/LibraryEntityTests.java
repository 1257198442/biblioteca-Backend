package com.example.demo.adapters.mongodb.entites;

import com.example.demo.TestConfig;
import com.example.demo.adapters.mongodb.entities.LibraryEntity;
import com.example.demo.domain.models.Library;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@TestConfig
public class LibraryEntityTests {
    @Test
    void testToLibrary(){
        LibraryEntity libraryEntity = LibraryEntity.builder()
                .address("X.º XX XXXXXXX, XX, 28001, XXXXXX, Madrid")
                .telephone("6669996669")
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
        Library library = Library.builder()
                .address("X.º XX XXXXXXX, XX, 28001, XXXXXX, Madrid")
                .telephone("6669996669")
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
        assertEquals(libraryEntity.toLibrary(),library);
    }

    @Test
    void testEquals() {
        LibraryEntity library1 = LibraryEntity.builder()
                .address("X.º XX XXXXXXX, XX, 28001, XXXXXX, Madrid")
                .telephone("6669996669")
                .name("BIBLIOTECA")
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

        LibraryEntity library2 = LibraryEntity.builder()
                .address("X.º XX XXXXXXX, XX, 28001, XXXXXX, Madrid")
                .telephone("6669996669")
                .name("BIBLIOTECA")
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

        LibraryEntity library3 = LibraryEntity.builder()
                .address("X.º XX XXXXXXX, XX, 28001, XXXXXX, Madrid")
                .telephone("6669996669")
                .name("BIBLIOTECA2")
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

        assertEquals(library1, library2);
        assertNotEquals(library1, library3);
    }

    @Test
    void testHashCode() {
        LibraryEntity library1 = LibraryEntity.builder()
                .address("X.º XX XXXXXXX, XX, 28001, XXXXXX, Madrid")
                .telephone("6669996669")
                .name("BIBLIOTECA")
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

        LibraryEntity library2 = LibraryEntity.builder()
                .address("X.º XX XXXXXXX, XX, 28001, XXXXXX, Madrid")
                .telephone("6669996669")
                .name("BIBLIOTECA")
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

        LibraryEntity library3 = LibraryEntity.builder()
                .address("X.º XX XXXXXXX, XX, 28001, XXXXXX, Madrid")
                .telephone("6669996669")
                .name("BIBLIOTECA2")
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

        assertEquals(library1.hashCode(), library2.hashCode());
        assertNotEquals(library1.hashCode(), library3.hashCode());
    }
}
