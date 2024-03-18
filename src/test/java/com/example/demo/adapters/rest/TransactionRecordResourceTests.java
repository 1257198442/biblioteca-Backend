package com.example.demo.adapters.rest;

import com.example.demo.domain.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;

@RestTestConfig
@ActiveProfiles({"test","dev"})
public class TransactionRecordResourceTests {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private JwtService jwtService;
    @Test
    void testReadByTelephone(){
        //401
        getReadByTelephoneClient("null","+34123").isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        getReadByTelephoneClient("Bearer "+jwtService.createToken("+34123","user","CLIENT"),"+34666666666").isEqualTo(HttpStatus.FORBIDDEN);
        //200
        getReadByTelephoneClient("Bearer "+jwtService.createToken("+34666666666","root","ROOT"),"+34123").isEqualTo(HttpStatus.OK);

    }

    StatusAssertions getReadByTelephoneClient(String token,String telephone){
        return  webTestClient.get()
                .uri("/transaction/search?telephone="+telephone )
                .header("Authorization", token)
                .exchange()
                .expectStatus();
    }
    @Test
    void testReadByReference(){
        //401
        getReadByReferenceClient("null","ijadlkfjsjf1").isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        getReadByReferenceClient("Bearer "+jwtService.createToken("+34666","user","CLIENT"),"ijadlkfjsjf1").isEqualTo(HttpStatus.FORBIDDEN);
        //404
        getReadByReferenceClient("Bearer "+jwtService.createToken("+34123","user","CLIENT"),"null").isEqualTo(HttpStatus.NOT_FOUND);
        //200
        getReadByReferenceClient("Bearer "+jwtService.createToken("+34666666666","root","ROOT"),"ijadlkfjsjf1").isEqualTo(HttpStatus.OK);

    }
    StatusAssertions getReadByReferenceClient(String token,String reference){
        return  webTestClient.get()
                .uri("/transaction/"+reference )
                .header("Authorization", token)
                .exchange()
                .expectStatus();
    }
}
