package com.example.demo.domain.models;

import com.example.demo.TestConfig;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@TestConfig
public class WalletTests {
    @Test
    void testEquals() {
        Wallet wallet1 = Wallet.builder().telephone("test").balance(new BigDecimal("12")).build();
        Wallet wallet2 = Wallet.builder().telephone("test").balance(new BigDecimal("14")).build();
        Wallet wallet3 = Wallet.builder().telephone("test").balance(new BigDecimal("12")).build();
        assertEquals(wallet1,wallet3);
        assertNotEquals(wallet1,wallet2);
    }
    @Test
    void testHashCode() {
        Wallet wallet1 = Wallet.builder().telephone("test").balance(new BigDecimal("12")).build();
        Wallet wallet2 = Wallet.builder().telephone("test").balance(new BigDecimal("14")).build();
        Wallet wallet3 = Wallet.builder().telephone("test").balance(new BigDecimal("12")).build();
        assertEquals(wallet1.hashCode(),wallet3.hashCode());
        assertNotEquals(wallet1.hashCode(),wallet2.hashCode());
    }
}
