package com.example.demo.adapters.rest;

import com.example.demo.adapters.rest.dto.BookUpdateDto;
import com.example.demo.adapters.rest.dto.BookUploadDto;
import com.example.demo.domain.models.BookStatus;
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
import java.math.BigDecimal;
import java.util.List;

@RestTestConfig
@ActiveProfiles({"test","dev"})
public class BookResourceTests {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private JwtService jwtService;
    @Test
    void testReadAll(){
        this.webTestClient
                .get()
                .uri("/book")
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.OK);
    }
    @Test
    void testGetAllLanguage(){
        this.webTestClient
                .get()
                .uri("/book/all_language")
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.OK);
    }
    @Test
    void testCreateAndDelete(){
        BookUploadDto bookUploadDto = BookUploadDto.builder()
                .name("testCreatePoint")
                .description("This is a test for creating a book port")
                .publisher("test publisher")
                .barcode("11223344")
                .isbn("abcd1234")
                .deposit(new BigDecimal("99"))
                .language("English")
                .authorId(List.of()).build();

        //200
        postCreateClient("Bearer "+jwtService.createToken("+34666000001","administrator","ADMINISTRATOR"),bookUploadDto).isEqualTo(HttpStatus.OK);
        //401
        postCreateClient("",bookUploadDto).isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        postCreateClient("Bearer "+jwtService.createToken("+34666000002","user","CLIENT"),bookUploadDto).isEqualTo(HttpStatus.FORBIDDEN);
        //422
        bookUploadDto.setLanguage("test");
        postCreateClient("Bearer "+jwtService.createToken("+34666666666","root","ROOT"),bookUploadDto).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        //401
        deleteClient("","5").isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        deleteClient("Bearer "+jwtService.createToken("+34666000002","user","CLIENT"),"5").isEqualTo(HttpStatus.FORBIDDEN);
        //404
        deleteClient("Bearer "+jwtService.createToken("+34666666666","root","ROOT"),"null").isEqualTo(HttpStatus.NOT_FOUND);
        //409
        deleteClient("Bearer "+jwtService.createToken("+34666666666","root","ROOT"),"5").isEqualTo(HttpStatus.CONFLICT);
        //200
        deleteClient("Bearer "+jwtService.createToken("+34666666666","root","ROOT"),"6").isEqualTo(HttpStatus.OK);
    }

    @Test
    void testRead(){
        //200
        getReadClient("1").isEqualTo(HttpStatus.OK);
        //404
        getReadClient("null").isEqualTo(HttpStatus.NOT_FOUND);

    }


    @Test
    void testUpdateImage() throws IOException {
        ClassPathResource file = new ClassPathResource("static/testImages/img.png");
        ClassPathResource file1 = new ClassPathResource("static/testImages/test.txt");
        ClassPathResource file2 = new ClassPathResource("static/testImages/fileSizeToLager.jpg");
        //401
        putUpdateImageClient("null","7",file).isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        putUpdateImageClient("Bearer "+jwtService.createToken("+34645321068","client","CLIENT"),"7",file).isEqualTo(HttpStatus.FORBIDDEN);
        //404
        putUpdateImageClient("Bearer "+jwtService.createToken("+34666666666","root","ROOT"),"null",file).isEqualTo(HttpStatus.NOT_FOUND);
        //413
        putUpdateImageClient("Bearer "+jwtService.createToken("+34666666666","root","ROOT"),"7",file2).isEqualTo(HttpStatus.PAYLOAD_TOO_LARGE);
        //422
        putUpdateImageClient("Bearer "+jwtService.createToken("+34666666666","root","ROOT"),"7",file1).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        //200
        putUpdateImageClient("Bearer "+jwtService.createToken("+34666666666","root","ROOT"),"7",file).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testUpdate(){
        BookUpdateDto bookUpdateDto = BookUpdateDto.builder()
                .name("testUpdatePoint")
                .description("description1")
                .publisher("publisher1")
                .barcode("11223344")
                .language("English")
                .isbn("abcd1234")
                .deposit(new BigDecimal("77"))
                .authorId(List.of())
                .bookType(List.of())
                .build();
        //401
        putUpdateClient("","4",bookUpdateDto).isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        putUpdateClient("Bearer "+jwtService.createToken("+34645321068","client","CLIENT"),"4",bookUpdateDto).isEqualTo(HttpStatus.FORBIDDEN);
        //404
        putUpdateClient("Bearer "+jwtService.createToken("+34666666666","root","ROOT"),"null",bookUpdateDto).isEqualTo(HttpStatus.NOT_FOUND);
        //200
        putUpdateClient("Bearer "+jwtService.createToken("+34666666666","root","ROOT"),"4",bookUpdateDto).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testModifyBookStatus(){
        //401
        putModifyBookStatus("","3", "ENABLE").isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        putModifyBookStatus("Bearer "+jwtService.createToken("+34645321068","client","CLIENT"),"3", "ENABLE").isEqualTo(HttpStatus.FORBIDDEN);
        //404
        putModifyBookStatus("Bearer "+jwtService.createToken("+34666666666","root","ROOT"),"null", "ENABLE").isEqualTo(HttpStatus.NOT_FOUND);
        //409
        putModifyBookStatus("Bearer "+jwtService.createToken("+34666000001","administrator","ADMINISTRATOR"),"3", "ENABLE").isEqualTo(HttpStatus.CONFLICT);
        //422
        putModifyBookStatus("Bearer "+jwtService.createToken("+34666000001","administrator","ADMINISTRATOR"),"1", "error").isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        //200
        putModifyBookStatus("Bearer "+jwtService.createToken("+34666666666","root","ROOT"),"3", "ENABLE").isEqualTo(HttpStatus.OK);
        putModifyBookStatus("Bearer "+jwtService.createToken("+34666000001","administrator","ADMINISTRATOR"),"3", "OCCUPIED").isEqualTo(HttpStatus.OK);

    }

    @Test
    void testRandomBook(){
        webTestClient.get()
            .uri("/book/random")
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.OK);
    }

    @Test
    void testReadByNameAndPublisherAndAuthor(){
        //200
        getReadByNameAndPublisherAndAuthor("/book/search?name=book").isEqualTo(HttpStatus.OK);
        //422
        getReadByNameAndPublisherAndAuthor("/book/search?language=error").isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        //200
        getReadByNameAndPublisherAndAuthor("/book/search?authorId=test").isEqualTo(HttpStatus.OK);
    }

    StatusAssertions getReadByNameAndPublisherAndAuthor(String url){
        return webTestClient.get()
                .uri(url)
                .exchange()
                .expectStatus();
    }

    StatusAssertions postCreateClient(String token, BookUploadDto bookUploadDto){
        return webTestClient.post()
                .uri("/book")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL)
                .bodyValue(bookUploadDto)
                .exchange()
                .expectStatus();
    }
    StatusAssertions getReadClient(String id){
        return webTestClient.get()
                .uri("/book/{id}",id)
                .exchange()
                .expectStatus();
    }

    StatusAssertions deleteClient(String token,String id){
        return webTestClient.delete()
                .uri("/book/{id}",id)
                .header("Authorization", token)
                .exchange()
                .expectStatus();
    }

    StatusAssertions putUpdateImageClient(String token, String id, ClassPathResource file) throws IOException {
        return webTestClient.put()
                .uri("/book/{id}/image",id)
                .header("Authorization", token)
                .contentType(MediaType.IMAGE_PNG)
                .body(BodyInserters.fromMultipartData("file", new FileSystemResource(file.getFile())))
                .exchange()
                .expectStatus();
    }

    StatusAssertions putUpdateClient(String token, String id, BookUpdateDto bookUpdateDto){
        return webTestClient.put()
                .uri("/book/{id}",id)
                .header("Authorization", token)
                .accept(MediaType.ALL)
                .bodyValue(bookUpdateDto)
                .exchange()
                .expectStatus();

    }

    StatusAssertions putModifyBookStatus(String token,String id,String status){
        return webTestClient.put()
                .uri("/book/{id}/status",id)
                .header("Authorization", token)
                .accept(MediaType.ALL)
                .bodyValue(status)
                .exchange()
                .expectStatus();
    }

}
