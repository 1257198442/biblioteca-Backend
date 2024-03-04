package com.example.demo.adapters.rest;

import com.example.demo.domain.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;

@RestTestConfig
@ActiveProfiles("test")
public class WalletResourceTests {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private JwtService jwtService;
    @Test
    void testRead(){
        //401
        getReadClient("","+34123").isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        getReadClient("Bearer "+jwtService.createToken("+34666000002","Client","CLIENT"),"+34123").isEqualTo(HttpStatus.FORBIDDEN);
        //404
        getReadClient("Bearer "+jwtService.createToken("+34666666666","ROOT","ROOT"),"null").isEqualTo(HttpStatus.NOT_FOUND);
        //201
        getReadClient("Bearer "+jwtService.createToken("+34666666666","ROOT","ROOT"),"+34123").isEqualTo(HttpStatus.OK);
    }
    StatusAssertions getReadClient(String token,String telephone){
        return this.webTestClient
                .get()
                .uri("/wallet/"+telephone )
                .header("Authorization", token)
                .exchange()
                .expectStatus();
    }
}
