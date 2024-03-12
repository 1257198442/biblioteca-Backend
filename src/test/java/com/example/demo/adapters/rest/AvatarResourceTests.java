package com.example.demo.adapters.rest;

import com.example.demo.domain.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@RestTestConfig
@ActiveProfiles({"test","dev"})
public class AvatarResourceTests {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private JwtService jwtService;

    @Test
    void testReadByTelephone(){
        //404
        getAvatarByTelephoneClient("null").isEqualTo(HttpStatus.NOT_FOUND);
        //200
        getAvatarByTelephoneClient("+34666").isEqualTo(HttpStatus.OK);
    }
    StatusAssertions getAvatarByTelephoneClient(String telephone){
        return  webTestClient.get()
                .uri("/avatar/"+telephone)
                .exchange()
                .expectStatus();
    }
}
