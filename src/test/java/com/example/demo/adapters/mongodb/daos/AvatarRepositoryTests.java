package com.example.demo.adapters.mongodb.daos;

import com.example.demo.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfig
public class AvatarRepositoryTests {
    @Autowired
    private AvatarRepository avatarRepository;
    @Test
    void testReadByTelephone(){
        assertTrue(avatarRepository.readByTelephone("+34123").isPresent());
        assertTrue(avatarRepository.readByTelephone("null").isEmpty());
    }
}
