package com.example.demo.domain.service;


import com.example.demo.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfig
class JwtServiceIT {

    @Autowired
    private JwtService jwtService;

    @Test
    void testJwtExceptionNotBearer() {
        assertTrue(jwtService.user("TEST-Not Bearer").isEmpty());
    }

    @Test
    void testJwtUtilExtract() {
        assertEquals("test1.1.1", jwtService.extractToken("Bearer test1.1.1"));
    }

    @Test
    void testCreateTokenAndVerify() {
        String token = jwtService.createToken("telephone", "name", "ROLE");
        assertEquals(3, token.split("\\.").length);
        assertTrue(token.length() > 30);
        assertEquals("telephone", jwtService.user(token));
        assertEquals("name", jwtService.name(token));
        assertEquals("ROLE", jwtService.role(token));
    }

}
