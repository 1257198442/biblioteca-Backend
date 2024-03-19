package com.example.demo.adapters.rest;

import com.example.demo.domain.models.Type;
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
public class TypeResourceTests {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private JwtService jwtService;

    @Test
    void testCreate(){
        Type type = Type.builder().name("testCreatePoint").description("test text").build();
        Type type1 = Type.builder().name("test").description("test text").build();
        //401
        postCreateClient("null",type).isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        postCreateClient("Bearer "+jwtService.createToken("+34645321068","client","CLIENT"),type).isEqualTo(HttpStatus.FORBIDDEN);
        //409
        postCreateClient("Bearer "+jwtService.createToken("+34666000001","administrator","ADMINISTRATOR"),type1).isEqualTo(HttpStatus.CONFLICT);
        //200
        postCreateClient("Bearer "+jwtService.createToken("+34666000001","administrator","ADMINISTRATOR"),type).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testRead(){
        //404
        getReadClient("null").isEqualTo(HttpStatus.NOT_FOUND);
        //200
        getReadClient("test").isEqualTo(HttpStatus.OK);
    }

    @Test
    void testReadAll(){
        this.webTestClient.get()
                .uri("type")
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.OK);
    }

    StatusAssertions postCreateClient(String token, Type type){
        return this.webTestClient.post()
                .uri("/type")
                .header("Authorization", token)
                .accept(MediaType.ALL)
                .bodyValue(type)
                .exchange()
                .expectStatus();
    }

    StatusAssertions getReadClient(String id){
        return this.webTestClient.get()
                .uri("/type/{id}",id)
                .exchange()
                .expectStatus();
    }
}
