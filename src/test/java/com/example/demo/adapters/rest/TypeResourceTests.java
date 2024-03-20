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
    void testCreateAndDelete(){
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
        //401
        deleteClient("null","testCreatePoint").isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        deleteClient("Bearer "+jwtService.createToken("+34645321068","client","CLIENT"),"testCreatePoint").isEqualTo(HttpStatus.FORBIDDEN);
        //404
        deleteClient("Bearer "+jwtService.createToken("+34666000001","administrator","ADMINISTRATOR"),"null").isEqualTo(HttpStatus.NOT_FOUND);
        //200
        deleteClient("Bearer "+jwtService.createToken("+34666000001","administrator","ADMINISTRATOR"),"testCreatePoint").isEqualTo(HttpStatus.OK);
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

    @Test
    void testUpdate(){
        //401
        putUpdateClient("null","test4","123").isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        putUpdateClient("Bearer "+jwtService.createToken("+34645321068","client","CLIENT"),"test4","123").isEqualTo(HttpStatus.FORBIDDEN);
        //404
        putUpdateClient("Bearer "+jwtService.createToken("+34666000001","administrator","ADMINISTRATOR"),"null","123").isEqualTo(HttpStatus.NOT_FOUND);
        //200
        putUpdateClient("Bearer "+jwtService.createToken("+34666000001","administrator","ADMINISTRATOR"),"test4","123").isEqualTo(HttpStatus.OK);
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
    StatusAssertions deleteClient(String token, String id){
        return this.webTestClient.delete()
                .uri("/type/{id}",id)
                .header("Authorization", token)
                .exchange()
                .expectStatus();
    }

    StatusAssertions putUpdateClient(String token, String id, String description){
        return this.webTestClient.put()
                .uri("/type/{id}",id)
                .header("Authorization", token)
                .bodyValue(description)
                .exchange()
                .expectStatus();
    }
}
