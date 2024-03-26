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

    @Test
    void testReadAdminReturnAndLendingByShow(){
        //401
        getReadAdminReturnAndLendingByShowClient("").isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        getReadAdminReturnAndLendingByShowClient("Bearer "+jwtService.createToken("+34666000002","user","CLIENT")).isEqualTo(HttpStatus.FORBIDDEN);
        //200
        getReadAdminReturnAndLendingByShowClient("Bearer "+jwtService.createToken("+34666666666","root","ROOT")).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testReadClientReturnAndLendingByShow(){
        //401
        getReadClientReturnAndLendingByShowClient("","+34990099009").isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        getReadClientReturnAndLendingByShowClient("Bearer "+jwtService.createToken("+34666000002","user","CLIENT"),"+34990099009").isEqualTo(HttpStatus.FORBIDDEN);
        //200
        getReadClientReturnAndLendingByShowClient("Bearer "+jwtService.createToken("+34990099009","user","CLIENT"),"+34990099009").isEqualTo(HttpStatus.OK);
    }

    @Test
    void testReadNoReturnByTelephone(){
        //401
        getReadNoReturnByTelephoneClient("","+34990099009").isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        getReadNoReturnByTelephoneClient("Bearer "+jwtService.createToken("+34666000002","user","CLIENT"),"+34990099009").isEqualTo(HttpStatus.FORBIDDEN);
        //200
        getReadNoReturnByTelephoneClient("Bearer "+jwtService.createToken("+34990099009","user","CLIENT"),"+34990099009").isEqualTo(HttpStatus.OK);
    }

    @Test
    void  testReadLendingStatistics(){
        //401
        getReadLendingStatisticsClient("").isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        getReadLendingStatisticsClient("Bearer "+jwtService.createToken("+34990099009","user","CLIENT")).isEqualTo(HttpStatus.FORBIDDEN);
        //200
        getReadLendingStatisticsClient("Bearer "+jwtService.createToken("+34666666666","root","ROOT")).isEqualTo(HttpStatus.OK);
    }

    @Test
    void  testReadLendingMonthlyCountsByThisYear(){
        //401
        getReadLendingMonthlyCountsByThisYear("").isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        getReadLendingMonthlyCountsByThisYear("Bearer "+jwtService.createToken("+34990099009","user","CLIENT")).isEqualTo(HttpStatus.FORBIDDEN);
        //200
        getReadLendingMonthlyCountsByThisYear("Bearer "+jwtService.createToken("+34666666666","root","ROOT")).isEqualTo(HttpStatus.OK);
    }

    @Test
    void  testReadLendingDailyCountsByThisWeek(){
        //401
        getReadLendingDailyCountsByThisWeek("").isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        getReadLendingDailyCountsByThisWeek("Bearer "+jwtService.createToken("+34990099009","user","CLIENT")).isEqualTo(HttpStatus.FORBIDDEN);
        //200
        getReadLendingDailyCountsByThisWeek("Bearer "+jwtService.createToken("+34666666666","root","ROOT")).isEqualTo(HttpStatus.OK);
    }

    @Test
    void  testReadLendingYearlyCounts(){
        //401
        getReadLendingYearlyCounts("").isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        getReadLendingYearlyCounts("Bearer "+jwtService.createToken("+34990099009","user","CLIENT")).isEqualTo(HttpStatus.FORBIDDEN);
        //200
        getReadLendingYearlyCounts("Bearer "+jwtService.createToken("+34666666666","root","ROOT")).isEqualTo(HttpStatus.OK);
    }

    @Test
    void  testBanUserByOverdueMax30Days(){
        //401
        postBanUserByOverdueMax30Days("").isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        postBanUserByOverdueMax30Days("Bearer "+jwtService.createToken("+34990099009","user","CLIENT")).isEqualTo(HttpStatus.FORBIDDEN);
        //200
        postBanUserByOverdueMax30Days("Bearer "+jwtService.createToken("+34666666666","root","ROOT")).isEqualTo(HttpStatus.OK);
    }

    @Test
    void  testReadUserByOverdueMax30Days(){
        //401
        getReadUserByOverdueMax30Days("").isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        getReadUserByOverdueMax30Days("Bearer "+jwtService.createToken("+34990099009","user","CLIENT")).isEqualTo(HttpStatus.FORBIDDEN);
        //200
        getReadUserByOverdueMax30Days("Bearer "+jwtService.createToken("+34666666666","root","ROOT")).isEqualTo(HttpStatus.OK);
    }

    @Test
    void  testSendEmailToUserByApproachingExpiryDate(){
        //401
        postSendEmailToUserByApproachingExpiryDate("").isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        postSendEmailToUserByApproachingExpiryDate("Bearer "+jwtService.createToken("+34990099009","user","CLIENT")).isEqualTo(HttpStatus.FORBIDDEN);
        //200
        postSendEmailToUserByApproachingExpiryDate("Bearer "+jwtService.createToken("+34666666666","root","ROOT")).isEqualTo(HttpStatus.OK);
    }

    @Test
    void  testSendEmailToUserByOrderOverdue(){
        //401
        postSendEmailToUserByOrderOverdue("").isEqualTo(HttpStatus.UNAUTHORIZED);
        //403
        postSendEmailToUserByOrderOverdue("Bearer "+jwtService.createToken("+34990099009","user","CLIENT")).isEqualTo(HttpStatus.FORBIDDEN);
        //200
        postSendEmailToUserByOrderOverdue("Bearer "+jwtService.createToken("+34666666666","root","ROOT")).isEqualTo(HttpStatus.OK);
    }

    StatusAssertions postCreateClient(String token, LendingDataUploadDto lendingDto){
        return webTestClient.post()
                .uri("/lending_data")
                .header("Authorization", token)
                .accept(MediaType.ALL)
                .bodyValue(lendingDto)
                .exchange()
                .expectStatus();
    }

    StatusAssertions getReadByTelephoneClient(String token,String telephone){
        return webTestClient.get()
                .uri("/lending_data/search?telephone={telephone}",telephone)
                .header("Authorization", token)
                .exchange()
                .expectStatus();
    }

    StatusAssertions getReadByReferenceClient(String token,String reference){
        return webTestClient.get()
                .uri("/lending_data/{id}",reference)
                .header("Authorization", token)
                .exchange().expectStatus();
    }

    StatusAssertions getReadAdminReturnAndLendingByShowClient(String token){
        return webTestClient.get()
                .uri("/lending_data/admin_return_and_lending")
                .header("Authorization", token)
                .exchange()
                .expectStatus();
    }

    StatusAssertions getReadClientReturnAndLendingByShowClient(String token,String telephone){
        return webTestClient.get()
                .uri("/lending_data/client_return_and_lending/search?telephone={telephone}",telephone)
                .header("Authorization", token)
                .exchange()
                .expectStatus();
    }

    StatusAssertions getReadNoReturnByTelephoneClient(String token,String telephone){
        return webTestClient.get()
                .uri("/lending_data/no_return/search?telephone={telephone}",telephone)
                .header("Authorization",token)
                .exchange()
                .expectStatus();
    }

    StatusAssertions getReadLendingStatisticsClient(String token){
        return webTestClient.get()
                .uri("/lending_data/lending_statistics")
                .header("Authorization",token)
                .exchange()
                .expectStatus();
    }

    StatusAssertions getReadLendingMonthlyCountsByThisYear(String token){
        return webTestClient.get()
                .uri("/lending_data/monthly_counts")
                .header("Authorization",token)
                .exchange()
                .expectStatus();
    }

    StatusAssertions getReadLendingDailyCountsByThisWeek(String token){
        return webTestClient.get()
                .uri("/lending_data/weekly_counts")
                .header("Authorization",token)
                .exchange()
                .expectStatus();
    }

    StatusAssertions getReadLendingYearlyCounts(String token){
        return webTestClient.get()
                .uri("/lending_data/yearly_counts")
                .header("Authorization",token)
                .exchange()
                .expectStatus();
    }

    StatusAssertions postBanUserByOverdueMax30Days(String token){
        return webTestClient.post()
                .uri("/lending_data/send_email_to_user_by_overdue_max_30day")
                .header("Authorization",token)
                .exchange()
                .expectStatus();
    }

    StatusAssertions getReadUserByOverdueMax30Days(String token){
        return webTestClient.get()
                .uri("/lending_data/read_lending_data_by_overdue_max_30day")
                .header("Authorization",token)
                .exchange()
                .expectStatus();
    }

    StatusAssertions postSendEmailToUserByApproachingExpiryDate(String token){
        return webTestClient.post()
                .uri("/lending_data//send_email_to_user_by_approaching_date")
                .header("Authorization",token)
                .exchange()
                .expectStatus();
    }

    StatusAssertions postSendEmailToUserByOrderOverdue(String token){
        return webTestClient.post()
                .uri("/lending_data/send_email_to_user_by_order_overdue")
                .header("Authorization",token)
                .exchange()
                .expectStatus();
    }

}
