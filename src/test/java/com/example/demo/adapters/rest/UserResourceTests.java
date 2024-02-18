package com.example.demo.adapters.rest;

import com.example.demo.adapters.rest.dto.UserUploadDto;
import com.example.demo.domain.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;


import java.util.Base64;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@RestTestConfig
@ActiveProfiles("test")
public class UserResourceTests {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private JwtService jwtService;

    @Test
    void testLogin(){
        String CorrectAccount = "666666666:6";
        //200
        testLoginClient(toBase64(CorrectAccount)).isOk();
        //401
        testLoginClient(toBase64("123456789:1")).isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        testLoginClient(toBase64("666:6")).isEqualTo(HttpStatus.FORBIDDEN);
    }
    String toBase64(String str){
       return "Basic "+Base64.getEncoder().encodeToString(str.getBytes());
    }
    StatusAssertions testLoginClient(String basicAuthValue){
        return webTestClient.post()
                .uri("/user/login")
                .header("Authorization", basicAuthValue)
                .accept(MediaType.ALL)
                .exchange() // 执行请求
                .expectStatus();
    }
    @Test
    void testRead(){
        //200
        testReadClient("666666666").isOk().expectBody(String.class).consumeWith(response -> {
            String responseBody = response.getResponseBody();
            assertNotNull(responseBody);
        });
        //404
        testReadClient("test").isEqualTo(HttpStatus.NOT_FOUND);
        //403
        webTestClient.get()
                .uri("/user/{ID}","666666666")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);

    }
    StatusAssertions testReadClient(String telephone){
        String user = "666666666";
        String name = "root";
        String role = "ROOT";
        String token ="Bearer "+jwtService.createToken(user, name, role);
        return webTestClient.get()
                .uri("/user/{ID}",telephone)
                .header("Authorization",token)
                .accept(MediaType.ALL)
                .exchange()
                .expectStatus();
    }
    @Test
    void testCreate(){
        String phoneNumberFormatNotLegal =
                "{ \"name\": \"test\", " +
                "\"password\": \"6\", " +
                "\"telephone\": \"test\", " +
                "\"email\": \"test@test.com\" }";
        String phoneNumberIsExists =
                "{ \"name\": \"string\", " +
                "\"password\": \"6\", " +
                "\"telephone\": \"+34645321068\", " +
                "\"email\": \"test@test.com\" }";
        String formatLegal =
                "{ \"name\": \"string\", " +
                "\"password\": \"6\", " +
                "\"telephone\": \"+34666444222\", " +
                "\"email\": \"test@test.com\" }";
        //422
        postCreateClient(phoneNumberFormatNotLegal).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        //409
        postCreateClient(phoneNumberIsExists).isEqualTo(HttpStatus.CONFLICT);
        //201
        postCreateClient(formatLegal).isCreated();
    }
    StatusAssertions postCreateClient(String requestBody){
        return webTestClient.post()
                .uri("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus();
    }

}

