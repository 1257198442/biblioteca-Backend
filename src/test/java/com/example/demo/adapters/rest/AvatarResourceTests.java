package com.example.demo.adapters.rest;

import com.example.demo.domain.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.io.IOException;

import static org.apache.tomcat.util.http.fileupload.FileUploadBase.MULTIPART;

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

    @Test
    public void testUpdateAvatar() throws IOException {
        ClassPathResource file = new ClassPathResource("static/testImages/img.png");
        ClassPathResource file1 = new ClassPathResource("static/testImages/test.txt");
        ClassPathResource file2 = new ClassPathResource("static/testImages/fileSizeToLager.jpg");
        //401
        putUpdateAvatarClient("null","+34123",file).isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        putUpdateAvatarClient("Bearer "+jwtService.createToken("+34645321068","client","CLIENT"),"+34123",file).isEqualTo(HttpStatus.FORBIDDEN);
        //404
        putUpdateAvatarClient("Bearer "+jwtService.createToken("+34666666666","root","ROOT"),"null",file).isEqualTo(HttpStatus.NOT_FOUND);
        //413
        putUpdateAvatarClient("Bearer "+jwtService.createToken("+34666666666","root","ROOT"),"+34123",file2).isEqualTo(HttpStatus.PAYLOAD_TOO_LARGE);
        //422
        putUpdateAvatarClient("Bearer "+jwtService.createToken("+34666666666","root","ROOT"),"+34123",file1).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        //200
        putUpdateAvatarClient("Bearer "+jwtService.createToken("+34666666666","root","ROOT"),"+34123",file).isEqualTo(HttpStatus.OK);
    }

    StatusAssertions putUpdateAvatarClient(String token,String telephone,ClassPathResource file) throws IOException {
        return webTestClient.put()
                .uri("/avatar/"+telephone)
                .header("Authorization", token)
                .contentType(MediaType.IMAGE_PNG)
                .body(BodyInserters.fromMultipartData("file", new FileSystemResource(file.getFile())))
                .exchange()
                .expectStatus();
    }

}
