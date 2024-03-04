package com.example.demo.adapters.mongodb.persistence;

import com.example.demo.TestConfig;
import com.example.demo.adapters.mongodb.entities.WalletEntity;
import com.example.demo.domain.models.Wallet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestConfig
public class WalletPersistenceMongodbTests {
    @Autowired
    private WalletPersistenceMongodb walletPersistenceMongodb;
    @Test
    void testRead(){
        Wallet wallet = walletPersistenceMongodb.read("+34123");
        assertEquals("+34123",wallet.getTelephone());
        assertEquals(new BigDecimal("235"),wallet.getBalance());
    }

}
