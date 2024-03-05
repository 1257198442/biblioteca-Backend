package com.example.demo.adapters.mongodb.persistence;

import com.example.demo.TestConfig;
import com.example.demo.adapters.mongodb.entities.TransactionRecordEntity;
import com.example.demo.domain.exceptions.ForbiddenException;
import com.example.demo.domain.models.TransactionDetails;
import com.example.demo.domain.models.TransactionRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestConfig
public class TransactionRecordPersistenceTests {
    @Autowired
    private TransactionRecordPersistenceMongodb transactionRecordPersistenceMongodb;
    @Autowired
    private WalletPersistenceMongodb walletPersistenceMongodb;
    @Test
    void testCreate(){
        TransactionDetails transactionDetails = TransactionDetails.builder()
                .billingAddress("Test")
                .city("Test")
                .firstName("JIAMING")
                .lastName("SHI")
                .postalCode("00000")
                .build();
        TransactionRecord transactionRecord = TransactionRecord.builder()
                .transactionDetails(transactionDetails)
                .amount(new BigDecimal("100"))
                .purpose("test")
                .reference("tteesstt1122")
                .timestampTime(LocalDateTime.of(2000,1,1,1,1,1))
                .telephone("+34666666666").build();
        transactionRecordPersistenceMongodb.create(transactionRecord);
        assertEquals(new BigDecimal("1099"),walletPersistenceMongodb.read("+34666666666").getBalance());
        transactionRecord.setAmount(new BigDecimal("-10000"));
        assertThrows(ForbiddenException.class, () -> transactionRecordPersistenceMongodb.create(transactionRecord));
    }
}
