package com.example.demo.adapters.rest;

import com.example.demo.adapters.rest.dto.BookUploadDto;
import com.example.demo.domain.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.List;

@RestTestConfig
@ActiveProfiles({"test","dev"})
public class BookResourceTests {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private JwtService jwtService;
    @Test
    void testReadAll(){
        this.webTestClient
                .get()
                .uri("/book")
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.OK);
    }
    @Test
    void testGetAllLanguage(){
        this.webTestClient
                .get()
                .uri("/book/all_language")
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.OK);
    }
    @Test
    void  testCreate(){
        BookUploadDto bookUploadDto = BookUploadDto.builder()
                .name("testCreatePoint")
                .description("This is a test for creating a book port")
                .publisher("test publisher")
                .barcode("11223344")
                .isbn("abcd1234")
                .deposit(new BigDecimal("99"))
                .language("English")
                .authorId(List.of()).build();

        //200
        postCreateClient("Bearer "+jwtService.createToken("+34666000001","administrator","ADMINISTRATOR"),bookUploadDto).isEqualTo(HttpStatus.OK);
        //401
        postCreateClient("",bookUploadDto).isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        postCreateClient("Bearer "+jwtService.createToken("+34666000002","user","CLIENT"),bookUploadDto).isEqualTo(HttpStatus.FORBIDDEN);
        //422
        bookUploadDto.setLanguage("test");
        postCreateClient("Bearer "+jwtService.createToken("+34666666666","root","ROOT"),bookUploadDto).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    void testRead(){
        //200
        getReadClient("1").isEqualTo(HttpStatus.OK);
        //404
        getReadClient("null").isEqualTo(HttpStatus.NOT_FOUND);

    }
    StatusAssertions postCreateClient(String token, BookUploadDto bookUploadDto){
        return webTestClient.post()
                .uri("/book")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL)
                .bodyValue(bookUploadDto)
                .exchange()
                .expectStatus();
    }
    StatusAssertions getReadClient(String id){
        return webTestClient.get()
                .uri("/book/{id}",id)
                .exchange()
                .expectStatus();
    }
}
