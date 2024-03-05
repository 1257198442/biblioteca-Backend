package com.example.demo.domain.service;

import com.example.demo.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
public class RandomStringServiceTests {
    @Autowired
    private RandomStringService randomStringService;
    @Test
    void testGenerateRandomString(){
        assertEquals(12,randomStringService.generateRandomString(12).length());
        assertNotEquals(randomStringService.generateRandomString(12),randomStringService.generateRandomString(12));
    }
}
