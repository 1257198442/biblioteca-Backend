package com.example.demo.adapters.rest;

import com.example.demo.adapters.rest.dto.LendingDataUploadDto;
import com.example.demo.domain.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestTestConfig
@ActiveProfiles({"test","dev"})
public class LendingDataResourceTests {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private JwtService jwtService;

    @Test
    public void testCreate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LendingDataUploadDto lendingUploadDto = LendingDataUploadDto.builder().password("error").bookId("10").limitTime(LocalDateTime.now().plusMonths(2).format(formatter)).telephone("+34990099009").build();
        //401
        postCreateClient("",lendingUploadDto).isEqualTo(HttpStatus.UNAUTHORIZED);
        postCreateClient("Bearer "+jwtService.createToken("+34990099009","user","CLIENT"),lendingUploadDto).isEqualTo(HttpStatus.UNAUTHORIZED);
        lendingUploadDto.setPassword("6");
        //403
        lendingUploadDto.setTelephone("+34123");
        postCreateClient("Bearer "+jwtService.createToken("+34666000002","user","CLIENT"),lendingUploadDto).isEqualTo(HttpStatus.FORBIDDEN);
        //404
        lendingUploadDto.setTelephone("+34990099009");
        lendingUploadDto.setBookId("null");
        postCreateClient("Bearer "+jwtService.createToken("+34666000001","Administrator","ADMINISTRATOR"),lendingUploadDto).isEqualTo(HttpStatus.NOT_FOUND);
        //422
        lendingUploadDto.setBookId("10");
        lendingUploadDto.setLimitTime(LocalDateTime.now().plusMonths(5).format(formatter));
        postCreateClient("Bearer "+jwtService.createToken("+34666000001","Administrator","ADMINISTRATOR"),lendingUploadDto).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        lendingUploadDto.setLimitTime(LocalDateTime.now().plusMonths(2).format(formatter));
        //200
        postCreateClient("Bearer "+jwtService.createToken("+34666000001","Administrator","ADMINISTRATOR"),lendingUploadDto).isEqualTo(HttpStatus.OK);
        //409
        postCreateClient("Bearer "+jwtService.createToken("+34666000001","Administrator","ADMINISTRATOR"),lendingUploadDto).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void testReadAll() {
        //200
        getReadAllClient("Bearer "+jwtService.createToken("+34666000001","Administrator","ADMINISTRATOR")).isEqualTo(HttpStatus.OK);
        //401
        getReadAllClient("").isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        getReadAllClient("Bearer "+jwtService.createToken("+34666000002","user","CLIENT")).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void testReadByTelephone(){
        //401
        getReadByTelephoneClient("","+34990099009").isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        getReadByTelephoneClient("Bearer "+jwtService.createToken("+34666000002","user","CLIENT"),"+34990099009").isEqualTo(HttpStatus.FORBIDDEN);
        //200
        getReadByTelephoneClient("Bearer "+jwtService.createToken("+34666000001","Administrator","ADMINISTRATOR"),"+34990099009").isEqualTo(HttpStatus.OK);
    }

    @Test
    void testReadByReference(){
        //401
        getReadByReferenceClient("","1").isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        getReadByReferenceClient("Bearer "+jwtService.createToken("+34666000002","user","CLIENT"),"1").isEqualTo(HttpStatus.FORBIDDEN);
        //404
        getReadByReferenceClient("Bearer "+jwtService.createToken("+34666000002","user","CLIENT"),"null").isEqualTo(HttpStatus.NOT_FOUND);
        //200
        getReadByReferenceClient("Bearer "+jwtService.createToken("+34990099009","user","CLIENT"),"1").isEqualTo(HttpStatus.OK);
    }

    StatusAssertions postCreateClient(String token, LendingDataUploadDto lendingDto){
        return webTestClient.post()
                .uri("/lendingData")
                .header("Authorization", token)
                .accept(MediaType.ALL)
                .bodyValue(lendingDto)
                .exchange()
                .expectStatus();
    }

    StatusAssertions getReadAllClient(String token){
        return webTestClient.get()
                .uri("/lendingData")
                .header("Authorization", token)
                .exchange()
                .expectStatus();
    }

    StatusAssertions getReadByTelephoneClient(String token,String telephone){
        return webTestClient.get()
                .uri("/lendingData/search?telephone={telephone}",telephone)
                .header("Authorization", token)
                .exchange()
                .expectStatus();
    }

    StatusAssertions getReadByReferenceClient(String token,String reference){
        return webTestClient.get()
                .uri("/lendingData/{id}",reference)
                .header("Authorization", token)
                .exchange().expectStatus();
    }
}
