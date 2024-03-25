package com.example.demo.adapters.rest;

import com.example.demo.adapters.rest.dto.BookDamageDegreeDto;
import com.example.demo.domain.models.Degree;
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
public class ReturnDataResourceTests {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private JwtService jwtService;

    @Test
    void testCreate(){
        //401
        postCreateClient("","2").isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        postCreateClient("Bearer "+jwtService.createToken("+34666000002","user","CLIENT"),"2").isEqualTo(HttpStatus.FORBIDDEN);
        //404
        postCreateClient("Bearer "+jwtService.createToken("+34990099009","user","CLIENT"),"null").isEqualTo(HttpStatus.NOT_FOUND);
        //200
        postCreateClient("Bearer "+jwtService.createToken("+34990099009","user","CLIENT"),"2").isEqualTo(HttpStatus.OK);
        //409
        postCreateClient("Bearer "+jwtService.createToken("+34990099009","user","CLIENT"),"2").isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void testRead(){
        //401
        getReadClient("","2").isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        getReadClient("Bearer "+jwtService.createToken("+34666000002","user","CLIENT"),"2").isEqualTo(HttpStatus.FORBIDDEN);
        //404
        getReadClient("Bearer "+jwtService.createToken("+34990099009","user","CLIENT"),"null").isEqualTo(HttpStatus.NOT_FOUND);
        //200
        getReadClient("Bearer "+jwtService.createToken("+34990099009","user","CLIENT"),"2").isEqualTo(HttpStatus.OK);
    }

    @Test
    void testBookIsReturnAndBookNoReturn(){
        BookDamageDegreeDto bookDamageDegreeDto = BookDamageDegreeDto.builder().degree("error").addendum("OK").build();
        //422
        putBookIsReturn("Bearer "+jwtService.createToken("+34666666666","root","ROOT"),"4",bookDamageDegreeDto).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        bookDamageDegreeDto.setDegree("PERFECT");
        //401
        putBookIsReturn("","5",bookDamageDegreeDto).isEqualTo(HttpStatus.UNAUTHORIZED);
        putBookNoReturn("","5").isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        putBookIsReturn("Bearer "+jwtService.createToken("+34990099009","user","CLIENT"),"4",bookDamageDegreeDto).isEqualTo(HttpStatus.FORBIDDEN);
        putBookNoReturn("Bearer "+jwtService.createToken("+34990099009","user","CLIENT"),"4").isEqualTo(HttpStatus.FORBIDDEN);
        //404
        putBookIsReturn("Bearer "+jwtService.createToken("+34666666666","root","ROOT"),"null",bookDamageDegreeDto).isEqualTo(HttpStatus.NOT_FOUND);
        putBookNoReturn("Bearer "+jwtService.createToken("+34666666666","root","ROOT"),"null").isEqualTo(HttpStatus.NOT_FOUND);
        //200
        putBookNoReturn("Bearer "+jwtService.createToken("+34666666666","root","ROOT"),"5").isEqualTo(HttpStatus.OK);
        putBookIsReturn("Bearer "+jwtService.createToken("+34666666666","root","ROOT"),"5",bookDamageDegreeDto).isEqualTo(HttpStatus.OK);
        //409
        putBookIsReturn("Bearer "+jwtService.createToken("+34666666666","root","ROOT"),"5",bookDamageDegreeDto).isEqualTo(HttpStatus.CONFLICT);
    }

    StatusAssertions postCreateClient(String token,String reference){
        return webTestClient.post()
                .uri("/returnData")
                .header("Authorization", token)
                .accept(MediaType.ALL)
                .bodyValue(reference)
                .exchange()
                .expectStatus();
    }

    StatusAssertions getReadClient(String token,String reference){
        return webTestClient.get()
                .uri("/returnData/{reference}",reference)
                .header("Authorization",token)
                .accept(MediaType.ALL)
                .exchange()
                .expectStatus();
    }

    StatusAssertions putBookIsReturn(String token, String reference, BookDamageDegreeDto bookDamageDegreeDto){
        return webTestClient.put()
                .uri("/returnData/{reference}/isReturn",reference)
                .header("Authorization",token)
                .accept(MediaType.ALL)
                .bodyValue(bookDamageDegreeDto)
                .exchange()
                .expectStatus();
    }

    StatusAssertions putBookNoReturn(String token,String reference){
        return  webTestClient.put()
                .uri("/returnData/{reference}/noReturn",reference)
                .header("Authorization",token)
                .exchange()
                .expectStatus();
    }
}
