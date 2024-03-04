package com.example.demo.adapters.mongodb.daos;

import com.example.demo.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfig
public class WalletRepositoryTests {
    @Autowired
    private WalletRepository walletRepository;
    @Test
    void testRead(){
        assertTrue(walletRepository.findByTelephone("+34123").isPresent());
        assertFalse(walletRepository.findByTelephone("null").isPresent());
    }
}
