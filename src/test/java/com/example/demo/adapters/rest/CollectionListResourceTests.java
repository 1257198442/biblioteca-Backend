package com.example.demo.adapters.rest;

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
public class CollectionListResourceTests {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private JwtService jwtService;

    @Test
    void testReadBookData(){
        //401
        getReadBookDataClient("","+34990099009").isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        getReadBookDataClient("Bearer "+jwtService.createToken("+34645321068","client","CLIENT"),"+34990099009").isEqualTo(HttpStatus.FORBIDDEN);
        //404
        getReadBookDataClient("Bearer "+jwtService.createToken("+34666666666","root","ROOT"),"null").isEqualTo(HttpStatus.NOT_FOUND);
        //200
        getReadBookDataClient("Bearer "+jwtService.createToken("+34666666666","root","ROOT"),"+34990099009").isEqualTo(HttpStatus.OK);
    }
    @Test
    void testRead(){
        //401
        getReadClient("","+34990099009").isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        getReadClient("Bearer "+jwtService.createToken("+34645321068","client","CLIENT"),"+34990099009").isEqualTo(HttpStatus.FORBIDDEN);
        //404
        getReadClient("Bearer "+jwtService.createToken("+34666666666","root","ROOT"),"null").isEqualTo(HttpStatus.NOT_FOUND);
        //200
        getReadClient("Bearer "+jwtService.createToken("+34666666666","root","ROOT"),"+34990099009").isEqualTo(HttpStatus.OK);
    }
    @Test
    void testAddBook(){
        //401
        putAddBookClient("","+34990099009","1").isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        putAddBookClient("Bearer "+jwtService.createToken("+34645321068","client","CLIENT"),"+34990099009","1").isEqualTo(HttpStatus.FORBIDDEN);
        //404
        putAddBookClient("Bearer "+jwtService.createToken("+34666666666","root","ROOT"),"null","1").isEqualTo(HttpStatus.NOT_FOUND);
        //200
        putAddBookClient("Bearer "+jwtService.createToken("+34666666666","root","ROOT"),"+34990099009","1").isEqualTo(HttpStatus.OK);
    }


    StatusAssertions getReadBookDataClient(String token,String telephone){
        return webTestClient.get()
                .uri("/collection_list/{telephone}/book",telephone)
                .header("Authorization",token)
                .exchange()
                .expectStatus();
    }

    StatusAssertions getReadClient(String token,String telephone){
        return webTestClient.get()
                .uri("/collection_list/{telephone}",telephone)
                .header("Authorization",token)
                .exchange()
                .expectStatus();
    }

    StatusAssertions putAddBookClient(String token,String telephone,String bookId){
        return webTestClient.put()
                .uri("/collection_list/{telephone}/add_book",telephone)
                .header("Authorization",token)
                .accept(MediaType.ALL)
                .bodyValue(bookId)
                .exchange()
                .expectStatus();
    }
}
