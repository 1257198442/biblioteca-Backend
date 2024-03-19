package com.example.demo.adapters.rest;

import com.example.demo.adapters.rest.dto.AuthorUploadDto;
import com.example.demo.domain.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;

@RestTestConfig
@ActiveProfiles({"test","dev"})
public class AuthorResourceTests {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private JwtService jwtService;

    @Test
    void testRead(){
        //200
        getReadClient("111").isEqualTo(HttpStatus.OK);
        //404
        getReadClient("null").isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testCreate(){
        AuthorUploadDto authorUploadDto = AuthorUploadDto.builder().name("test create point").nationality("English").description("test text").build();
        //200
        postCreatClient("Bearer "+jwtService.createToken("+34666000001","administrator","ADMINISTRATOR"),authorUploadDto).isEqualTo(HttpStatus.OK);
        //401
        postCreatClient("",authorUploadDto).isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        postCreatClient("Bearer "+jwtService.createToken("+34666000002","user","CLIENT"),authorUploadDto).isEqualTo(HttpStatus.FORBIDDEN);

    }

    @Test
    void testReadAll(){
        this.webTestClient.get()
                .uri("/author")
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.OK);
    }

    StatusAssertions getReadClient(String id){
        return this.webTestClient.get()
                .uri("/author/{id}",id)
                .exchange()
                .expectStatus();
    }

    StatusAssertions postCreatClient(String token, AuthorUploadDto authorUploadDto){
        return this.webTestClient.post()
                .uri("/author")
                .header("Authorization", token)
                .accept(MediaType.ALL)
                .bodyValue(authorUploadDto)
                .exchange()
                .expectStatus();
    }
}
