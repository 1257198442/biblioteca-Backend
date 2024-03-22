package com.example.demo.adapters.rest;

import com.example.demo.adapters.rest.dto.AuthorUploadDto;
import com.example.demo.domain.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.io.IOException;

@RestTestConfig
@ActiveProfiles({"test","dev"})
public class AuthorResourceTests {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private JwtService jwtService;

    @Test
    void testRead(){
        //200
        getReadClient("111").isEqualTo(HttpStatus.OK);
        //404
        getReadClient("null").isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testCreate(){
        AuthorUploadDto authorUploadDto = AuthorUploadDto.builder().name("test create point").nationality("English").description("test text").build();
        //200
        postCreatClient("Bearer "+jwtService.createToken("+34666000001","administrator","ADMINISTRATOR"),authorUploadDto).isEqualTo(HttpStatus.OK);
        //401
        postCreatClient("",authorUploadDto).isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        postCreatClient("Bearer "+jwtService.createToken("+34666000002","user","CLIENT"),authorUploadDto).isEqualTo(HttpStatus.FORBIDDEN);

    }

    @Test
    void testReadAll(){
        this.webTestClient.get()
                .uri("/author")
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.OK);
    }

    @Test
    void testUpdate(){
        AuthorUploadDto authorUploadDto = AuthorUploadDto.builder().name("test update point").nationality("English").description("test text").build();
        //200
        putUpdateClient("Bearer "+jwtService.createToken("+34666000001","administrator","ADMINISTRATOR"),authorUploadDto,"111").isEqualTo(HttpStatus.OK);
        //401
        putUpdateClient("null",authorUploadDto,"111").isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        putUpdateClient("Bearer "+jwtService.createToken("+34666000002","user","CLIENT"),authorUploadDto,"111").isEqualTo(HttpStatus.FORBIDDEN);
        //404
        putUpdateClient("Bearer "+jwtService.createToken("+34666000001","administrator","ADMINISTRATOR"),authorUploadDto,"null").isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testDelete(){
        //200
        deleteClient("Bearer "+jwtService.createToken("+34666000001","administrator","ADMINISTRATOR"),"delete").isEqualTo(HttpStatus.OK);
        //401
        deleteClient("null","111").isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        deleteClient("Bearer "+jwtService.createToken("+34666000002","user","CLIENT"),"111").isEqualTo(HttpStatus.FORBIDDEN);
        //404
        deleteClient("Bearer "+jwtService.createToken("+34666000001","administrator","ADMINISTRATOR"),"null").isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testUpdateAuthorImage(){
        ClassPathResource file = new ClassPathResource("static/testImages/img.png");
        ClassPathResource file1 = new ClassPathResource("static/testImages/test.txt");
        ClassPathResource file2 = new ClassPathResource("static/testImages/fileSizeToLager.jpg");
        //401
        putUpdateAuthorImageClient("null","111",file).isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        putUpdateAuthorImageClient("Bearer "+jwtService.createToken("+34666000002","client","CLIENT"),"update",file).isEqualTo(HttpStatus.FORBIDDEN);
        //404
        putUpdateAuthorImageClient("Bearer "+jwtService.createToken("+34666000001","client","ADMINISTRATOR"),"null",file).isEqualTo(HttpStatus.NOT_FOUND);
        //413
        putUpdateAuthorImageClient("Bearer "+jwtService.createToken("+34666000001","client","ADMINISTRATOR"),"update",file2).isEqualTo(HttpStatus.PAYLOAD_TOO_LARGE);
        //422
        putUpdateAuthorImageClient("Bearer "+jwtService.createToken("+34666000001","client","ADMINISTRATOR"),"update",file1).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        //200
        putUpdateAuthorImageClient("Bearer "+jwtService.createToken("+34666000001","client","ADMINISTRATOR"),"update",file).isEqualTo(HttpStatus.OK);

        deleteClient("Bearer "+jwtService.createToken("+34666000001","client","ADMINISTRATOR"),"update").isEqualTo(HttpStatus.OK);
    }

    StatusAssertions getReadClient(String id){
        return this.webTestClient.get()
                .uri("/author/{id}",id)
                .exchange()
                .expectStatus();
    }

    StatusAssertions postCreatClient(String token, AuthorUploadDto authorUploadDto){
        return this.webTestClient.post()
                .uri("/author")
                .header("Authorization", token)
                .accept(MediaType.ALL)
                .bodyValue(authorUploadDto)
                .exchange()
                .expectStatus();
    }

    StatusAssertions putUpdateClient(String token, AuthorUploadDto authorUploadDto, String id){
        return this.webTestClient.put()
                .uri("/author/{id}",id)
                .header("Authorization", token)
                .accept(MediaType.ALL)
                .bodyValue(authorUploadDto)
                .exchange()
                .expectStatus();
    }

    StatusAssertions deleteClient(String token,String id){
        return this.webTestClient.delete()
                .uri("/author/{id}",id)
                .header("Authorization", token)
                .exchange()
                .expectStatus();
    }

    StatusAssertions putUpdateAuthorImageClient(String token, String id, ClassPathResource file){
        return this.webTestClient.put()
                .uri("/author/{id}/image",id)
                .header("Authorization", token)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("file", file))
                .exchange()
                .expectStatus();
    }
}
