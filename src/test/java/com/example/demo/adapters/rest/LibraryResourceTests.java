package com.example.demo.adapters.rest;

import com.example.demo.adapters.rest.dto.LibraryUpdateDto;
import com.example.demo.domain.models.Library;
import com.example.demo.domain.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RestTestConfig
@ActiveProfiles("test")
public class LibraryResourceTests {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private JwtService jwtService;
    @Test
    void testRead(){
        getRead().isEqualTo(HttpStatus.OK);
    }
    StatusAssertions getRead(){
        return this.webTestClient
                .get()
                .uri("/library")
                .exchange()
                .expectStatus();
    }
    @Test
    void testUpdate(){
        LibraryUpdateDto libraryUpdateDto1 = LibraryUpdateDto.builder()
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
                .discord("https://www.discord.com/").build();
        LibraryUpdateDto libraryUpdateDto2 = LibraryUpdateDto.builder()
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
                .discord("https://www.discord.com/").build();
        //403
        putUpdateLibrary("Bearer "+jwtService.createToken("+34666000001","Administrator","ADMINISTRATOR"),libraryUpdateDto1).isEqualTo(HttpStatus.FORBIDDEN);
        //403
        putUpdateLibrary("Bearer "+jwtService.createToken("+34666000002","User","CLIENT"),libraryUpdateDto1).isEqualTo(HttpStatus.FORBIDDEN);
        //200
        putUpdateLibrary("Bearer "+jwtService.createToken("+3466666666","root","ROOT"),libraryUpdateDto1).isOk();
        getRead().isOk().expectBody(Library.class)
                .consumeWith(response ->{
                    String telephone = response.getResponseBody().getTelephone();
                    assertEquals(libraryUpdateDto1.getTelephone(),telephone);
                });
        //200
        putUpdateLibrary("Bearer "+jwtService.createToken("+3466666666","root","ROOT"),libraryUpdateDto2).isOk();
    }

    StatusAssertions putUpdateLibrary(String token, LibraryUpdateDto libraryUpdateDto) {
        return this.webTestClient
                .put()
                .uri("/library")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL)
                .bodyValue(libraryUpdateDto)
                .exchange()
                .expectStatus();
    }
}
