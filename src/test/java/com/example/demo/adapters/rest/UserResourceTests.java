package com.example.demo.adapters.rest;

import com.example.demo.adapters.rest.dto.SettingUpdateDto;
import com.example.demo.adapters.rest.dto.UserUpdateDto;
import com.example.demo.domain.models.Role;
import com.example.demo.domain.models.User;
import com.example.demo.domain.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

@RestTestConfig
@ActiveProfiles("test")
public class UserResourceTests {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private JwtService jwtService;

    @Test
    void testLogin(){
        String CorrectAccount = "+34666666666:6";
        //200
        testLoginClient(toBase64(CorrectAccount)).isOk();
        //401
        testLoginClient(toBase64("123456789:1")).isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        testLoginClient(toBase64("+34666:6")).isEqualTo(HttpStatus.FORBIDDEN);
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
        getReadClient("+34666666666").isOk().expectBody(String.class).consumeWith(response -> {
            String responseBody = response.getResponseBody();
            assertNotNull(responseBody);
        });
        //404
        getReadClient("test").isEqualTo(HttpStatus.NOT_FOUND);
        //403
        webTestClient.get()
                .uri("/user/{ID}","+34666666666")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);

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
    @Test
    void testUpdateAdmin(){
        this.getReadClient("+34123").isOk()
                .expectBody(User.class)
                .consumeWith(response -> {
                    User user = response.getResponseBody();
                    assertEquals(Role.CLIENT, user.getRole());
                });
        putRoleUpdateClient("Bearer "+jwtService.createToken("+34666666666","root","ROOT"),"+34123","BAN").isOk();
        this.getReadClient("+34123").isOk()
                .expectBody(User.class)
                .consumeWith(response -> {
                    User user = response.getResponseBody();
                    assertEquals(Role.BAN, user.getRole());
                });
        //404
        putRoleUpdateClient("Bearer "+jwtService.createToken("+3466666666","root","ROOT"),"test","CLIENT").isEqualTo(HttpStatus.NOT_FOUND);
        //422
        putRoleUpdateClient("Bearer "+jwtService.createToken("+3466666666","root","ROOT"),"+34123","TEST").isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        //403
        putRoleUpdateClient("Bearer "+jwtService.createToken("+3466666666","root","ROOT"),"+34123","ROOT").isEqualTo(HttpStatus.FORBIDDEN);
        //403
        putRoleUpdateClient("Bearer "+jwtService.createToken("+34666000002","user","CLIENT"),"+34123","ADMINISTRATOR").isEqualTo(HttpStatus.FORBIDDEN);

    }
    @Test
    void testReadAll(){
        String user = "+34666666666";
        String name = "root";
        String role = "ROOT";
        String token ="Bearer "+jwtService.createToken(user, name, role);
        webTestClient.get()
                .uri("/user")
                .header("Authorization",token)
                .accept(MediaType.ALL)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ArrayList.class)
                .consumeWith(response ->{
                    assertNotNull(response.getResponseBody());
                });
    }
    @Test
    void testUpdate(){
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .description("t")
                .birthdays(LocalDate.of(2020,1,1))
                .email("test@testtest.com")
                .name("123").build();
        //401
        putUpdateClient("","+34123",userUpdateDto).isEqualTo(HttpStatus.UNAUTHORIZED);
        //404
        putUpdateClient("Bearer "+jwtService.createToken("+3466666666","root","ROOT"),"null",userUpdateDto).isEqualTo(HttpStatus.NOT_FOUND);
        //403
        putUpdateClient("Bearer "+jwtService.createToken("+34645321068","client","CLIENT"),"+34123",userUpdateDto).isEqualTo(HttpStatus.FORBIDDEN);
        //200
        putUpdateClient("Bearer "+jwtService.createToken("+3466666666","root","ROOT"),"+34123",userUpdateDto).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testUpdateSetting(){
        SettingUpdateDto settingUpdateDto = SettingUpdateDto.builder().hideMyProfile(false).emailWhenOrderIsGenerated(true).build();
        SettingUpdateDto settingUpdateDto1 = SettingUpdateDto.builder().hideMyProfile(true).emailWhenOrderIsGenerated(true).build();
        //401
        putUpdateClient("","+34123",settingUpdateDto).isEqualTo(HttpStatus.UNAUTHORIZED);
        //404
        putUpdateClient("Bearer "+jwtService.createToken("+3466666666","root","ROOT"),"null",settingUpdateDto).isEqualTo(HttpStatus.NOT_FOUND);
        //403
        putUpdateClient("Bearer "+jwtService.createToken("+34645321068","client","CLIENT"),"+34123",settingUpdateDto).isEqualTo(HttpStatus.FORBIDDEN);
        //200
        putUpdateClient("Bearer "+jwtService.createToken("+3466666666","root","ROOT"),"+34666",settingUpdateDto).isEqualTo(HttpStatus.OK);
        putUpdateClient("Bearer "+jwtService.createToken("+3466666666","root","ROOT"),"+34666",settingUpdateDto1).isEqualTo(HttpStatus.OK);
    }

    StatusAssertions getReadClient(String telephone){
        String user = "+34666666666";
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

    StatusAssertions postCreateClient(String requestBody){
        return webTestClient.post()
                .uri("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus();
    }



    StatusAssertions putRoleUpdateClient(String token, String telephone, String body) {
        return this.webTestClient
                .put()
                .uri("/user/"+telephone + "/role")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL)
                .bodyValue(body)
                .exchange()
                .expectStatus();
    }

    StatusAssertions putUpdateClient(String token, String telephone, UserUpdateDto body) {
        return this.webTestClient
                .put()
                .uri("/user/"+telephone)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL)
                .bodyValue(body)
                .exchange()
                .expectStatus();

    }

    StatusAssertions putUpdateClient(String token, String telephone, SettingUpdateDto body) {
        return this.webTestClient
                .put()
                .uri("/user/"+telephone+"/setting")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL)
                .bodyValue(body)
                .exchange()
                .expectStatus();

    }
}

