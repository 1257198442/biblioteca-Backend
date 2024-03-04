package com.example.demo.domain.service;

import com.example.demo.TestConfig;
import com.example.demo.domain.exceptions.NotFoundException;
import com.example.demo.domain.models.Wallet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;


@TestConfig
public class WalletServiceTests {
    @Autowired
    private WalletService walletService;
    @Test
    void testRead(){
        Wallet wallet = walletService.readByTelephone("+34666");
        //404
        assertThrows(NotFoundException.class,()->{
            walletService.readByTelephone("null");
        });
        assertEquals("+34666",wallet.getTelephone());
        assertEquals(new BigDecimal("0"),wallet.getBalance());
    }
}
