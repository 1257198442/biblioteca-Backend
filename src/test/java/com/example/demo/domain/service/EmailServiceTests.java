package com.example.demo.domain.service;

import com.example.demo.adapters.rest.RestTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RestTestConfig
@ActiveProfiles("test")
class EmailServiceTests {
    @Autowired
    private EmailService emailService;
    @Test
    void testSendEmail(){
        emailService.isSimulated=true;
        emailService.sendEmail("12571984422@qq.com","testEmail","this is a testEmail");
        assertTrue(true);
    }



}