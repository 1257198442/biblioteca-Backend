package com.example.demo.domain.service;

import com.example.demo.TestConfig;
import com.example.demo.domain.exceptions.ForbiddenException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
public class UserServiceIT {
    @Autowired
    private UserService userService;
    @Test
    void testLogin(){
        assertNotNull(userService.login("+34645321068"));
        assertThrows(ForbiddenException.class, () -> {
            userService.login("+34666000002");
        });
    }
}
