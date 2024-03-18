package com.example.demo.adapters.rest.dto;

import com.example.demo.TestConfig;
import com.example.demo.adapters.mongodb.entities.TransactionRecordEntity;
import com.example.demo.domain.models.TransactionDetails;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@TestConfig
public class TransactionRecordDtoTests {
    @Test
    void testEquals() {
        TransactionDetails transactionDetails = TransactionDetails.builder()
                .billingAddress("Test")
                .city("Test")
                .firstName("JIAMING")
                .lastName("SHI")
                .postalCode("00000")
                .build();
        TransactionRecordDto transactionRecord1 = TransactionRecordDto.builder()
                .transactionDetails(transactionDetails)
                .amount(new BigDecimal("100"))
                .purpose("test")
                .telephone("+341212121212").build();
        TransactionRecordDto transactionRecord2 = TransactionRecordDto.builder()
                .transactionDetails(transactionDetails)
                .amount(new BigDecimal("100"))
                .purpose("test")
                .telephone("+341212121212").build();
        TransactionRecordDto transactionRecord3 = TransactionRecordDto.builder()
                .transactionDetails(transactionDetails)
                .amount(new BigDecimal("200"))
                .purpose("test")
                .telephone("+341212121212").build();
        assertEquals(transactionRecord1,transactionRecord2);
        assertNotEquals(transactionRecord1,transactionRecord3);
    }

    @Test
    void testHashcode() {
        TransactionDetails transactionDetails = TransactionDetails.builder()
                .billingAddress("Test")
                .city("Test")
                .firstName("JIAMING")
                .lastName("SHI")
                .postalCode("00000")
                .build();
        TransactionRecordDto transactionRecord1 = TransactionRecordDto.builder()
                .transactionDetails(transactionDetails)
                .amount(new BigDecimal("100"))
                .purpose("test")
                .telephone("+341212121212").build();
        TransactionRecordDto transactionRecord2 = TransactionRecordDto.builder()
                .transactionDetails(transactionDetails)
                .amount(new BigDecimal("100"))
                .purpose("test")
                .telephone("+341212121212").build();
        TransactionRecordDto transactionRecord3 = TransactionRecordDto.builder()
                .transactionDetails(transactionDetails)
                .amount(new BigDecimal("200"))
                .purpose("test")
                .telephone("+341212121212").build();
        assertEquals(transactionRecord1.hashCode(),transactionRecord2.hashCode());
        assertNotEquals(transactionRecord1.hashCode(),transactionRecord3.hashCode());
    }

}
