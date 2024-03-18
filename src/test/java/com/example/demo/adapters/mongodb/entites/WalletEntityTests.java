package com.example.demo.adapters.mongodb.entites;

import com.example.demo.TestConfig;
import com.example.demo.adapters.mongodb.entities.WalletEntity;
import com.example.demo.domain.models.Wallet;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@TestConfig
public class WalletEntityTests {
    @Test
    void testToWallet(){
        WalletEntity walletEntity = WalletEntity.builder().telephone("test").balance(new BigDecimal("12")).build();
        Wallet wallet = Wallet.builder().telephone("test").balance(new BigDecimal("12")).build();
        assertEquals(wallet,walletEntity.toWallet());
    }
    @Test
    void testEquals() {
        WalletEntity wallet1 = WalletEntity.builder().telephone("test").balance(new BigDecimal("12")).build();
        WalletEntity wallet2 = WalletEntity.builder().telephone("test").balance(new BigDecimal("14")).build();
        WalletEntity wallet3 = WalletEntity.builder().telephone("test").balance(new BigDecimal("12")).build();
        assertEquals(wallet1,wallet3);
        assertNotEquals(wallet1,wallet2);
    }
    @Test
    void testHashCode() {
        WalletEntity wallet1 = WalletEntity.builder().telephone("test").balance(new BigDecimal("12")).build();
        WalletEntity wallet2 = WalletEntity.builder().telephone("test").balance(new BigDecimal("14")).build();
        WalletEntity wallet3 = WalletEntity.builder().telephone("test").balance(new BigDecimal("12")).build();
        assertEquals(wallet1.hashCode(),wallet3.hashCode());
        assertNotEquals(wallet1.hashCode(),wallet2.hashCode());
    }
}
