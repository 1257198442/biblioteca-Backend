package com.example.demo.adapters.rest;

import com.example.demo.adapters.rest.dto.TransactionRecordDto;
import com.example.demo.domain.models.TransactionDetails;
import com.example.demo.domain.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

@RestTestConfig
@ActiveProfiles({"test","dev"})
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
    @Test
    void testRecharge(){
        TransactionDetails transactionDetails = TransactionDetails.builder()
                .postalCode("00000")
                .lastName("JIAMING")
                .firstName("SHI")
                .city("MADRID")
                .billingAddress("TEST").build();
        TransactionRecordDto transactionRecordDto = TransactionRecordDto.builder()
                .transactionDetails(transactionDetails)
                .amount(new BigDecimal("10000"))
                .purpose("test")
                .telephone("+34321").build();
        //200
        postRechargeClient("Bearer "+jwtService.createToken("+34666666666","user","ROOT"),transactionRecordDto).isEqualTo(HttpStatus.OK);
        //401
        postRechargeClient("",transactionRecordDto).isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        postRechargeClient("Bearer "+jwtService.createToken("+34666000002","Client","CLIENT"),transactionRecordDto).isEqualTo(HttpStatus.FORBIDDEN);
        //404
        transactionRecordDto.setTelephone("null");
        postRechargeClient("Bearer "+jwtService.createToken("+34666666666","ROOT","ROOT"),transactionRecordDto).isEqualTo(HttpStatus.NOT_FOUND);
        //422
        transactionRecordDto.setTelephone("+34123");
        transactionRecordDto.setAmount(new BigDecimal("-100"));
        postRechargeClient("Bearer "+jwtService.createToken("+34123","user","CLIENT"),transactionRecordDto).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    StatusAssertions postRechargeClient(String token, TransactionRecordDto transactionRecordDto){
        return this.webTestClient
                .post()
                .uri("/wallet/recharge")
                .header("Authorization", token)
                .bodyValue(transactionRecordDto)
                .exchange()
                .expectStatus();
    }

    @Test
    void testWithdrawal(){
        TransactionDetails transactionDetails = TransactionDetails.builder()
                .postalCode("00000")
                .lastName("JIAMING")
                .firstName("SHI")
                .city("MADRID")
                .billingAddress("TEST").build();
        TransactionRecordDto transactionRecordDto = TransactionRecordDto.builder()
                .password("6")
                .transactionDetails(transactionDetails)
                .amount(new BigDecimal("1000"))
                .purpose("test")
                .telephone("+34321").build();
        //200
        postWithdrawalClient("Bearer "+jwtService.createToken("+34666666666","user","ROOT"),transactionRecordDto).isEqualTo(HttpStatus.OK);
        //401
        postWithdrawalClient("",transactionRecordDto).isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        postWithdrawalClient("Bearer "+jwtService.createToken("+34666000002","Client","CLIENT"),transactionRecordDto).isEqualTo(HttpStatus.FORBIDDEN);
        transactionRecordDto.setAmount(new BigDecimal("1000000"));
        postWithdrawalClient("Bearer "+jwtService.createToken("+34123","user","CLIENT"),transactionRecordDto).isEqualTo(HttpStatus.FORBIDDEN);
        //404
        transactionRecordDto.setTelephone("null");
        postWithdrawalClient("Bearer "+jwtService.createToken("+34666666666","ROOT","ROOT"),transactionRecordDto).isEqualTo(HttpStatus.NOT_FOUND);
        //422
        transactionRecordDto.setTelephone("+34123");
        transactionRecordDto.setAmount(new BigDecimal("-100"));
        postWithdrawalClient("Bearer "+jwtService.createToken("+34123","user","CLIENT"),transactionRecordDto).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }
    StatusAssertions postWithdrawalClient(String token, TransactionRecordDto transactionRecordDto){
        return this.webTestClient
                .post()
                .uri("/wallet/withdrawal")
                .header("Authorization", token)
                .bodyValue(transactionRecordDto)
                .exchange()
                .expectStatus();
    }
}
