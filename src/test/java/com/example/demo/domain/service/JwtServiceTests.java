package com.example.demo.domain.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@TestConfig
class JwtServiceTests {
    private JwtService jwtService;
    private String secret = "666";
    private String issuer = "JIAMING.SHI";
    private int expire = 600; // 10 minutes

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(secret, issuer, expire);
    }

    @Test
    void testCreateToken() {
        String user = "testUser";
        String name = "Test Name";
        String role = "USER";
        String token = jwtService.createToken(user, name, role);
        assertNotNull(token);
        assertFalse(token.isEmpty());

        DecodedJWT jwt = JWT.require(Algorithm.HMAC256(secret))
                .withIssuer(issuer)
                .build()
                .verify(token);
        assertEquals(user, jwt.getClaim("user").asString());
        assertEquals(name, jwt.getClaim("name").asString());
        assertEquals(role, jwt.getClaim("role").asString());
        assertTrue(jwt.getExpiresAt().after(new Date()));
    }

    @Test
    void testExtractToken() {
        String expectedToken = "1.1.1";
        String bearerToken = "Bearer " + expectedToken;
        assertEquals(expectedToken, jwtService.extractToken(bearerToken));
    }

    @Test
    void testUser() {
        String user = "testUser";
        String name = "Test Name";
        String role = "USER";
        String token = jwtService.createToken(user, name, role);
        assertEquals(user, jwtService.user(token));
    }

    @Test
    void testName() {
        String user = "testUser";
        String name = "Test Name";
        String role = "USER";
        String token = jwtService.createToken(user, name, role);
        assertEquals(name, jwtService.name(token));
    }

    @Test
    void testRole() {
        String user = "testUser";
        String name = "Test Name";
        String role = "USER";
        String token = jwtService.createToken(user, name, role);
        assertEquals(role, jwtService.role(token));
    }

}
