package com.example.demo.adapters.mongodb.entites;

import com.example.demo.TestConfig;
import com.example.demo.adapters.mongodb.entities.TransactionRecordEntity;
import com.example.demo.domain.models.TransactionDetails;
import com.example.demo.domain.models.TransactionRecord;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@TestConfig
public class TransactionRecordEntityTests {

//    public String reference;
//    public String purpose;
//    public String telephone;
//    public BigDecimal amount;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime timestampTime;
//    private TransactionDetails transactionDetails;

    @Test
    void testToTransactionRecord(){
        TransactionDetails transactionDetails = TransactionDetails.builder()
            .billingAddress("Test")
            .city("Test")
            .firstName("JIAMING")
            .lastName("SHI")
            .postalCode("00000")
            .build();
        TransactionRecordEntity transactionRecord1 = TransactionRecordEntity.builder()
                .transactionDetails(transactionDetails)
                .amount(new BigDecimal("100"))
                .purpose("test")
                .reference("tteesstt1122")
                .timestampTime(LocalDateTime.of(2000,1,1,1,1,1))
                .telephone("+341212121212").build();
        TransactionRecord transactionRecord2 = TransactionRecord.builder()
                .transactionDetails(transactionDetails)
                .amount(new BigDecimal("100"))
                .purpose("test")
                .reference("tteesstt1122")
                .timestampTime(LocalDateTime.of(2000,1,1,1,1,1))
                .telephone("+341212121212").build();
        assertEquals(transactionRecord2,transactionRecord1.toTransactionRecord());
        assertEquals(transactionRecord1,new TransactionRecordEntity(transactionRecord2));
    }

    @Test
    void testEquals() {
        TransactionDetails transactionDetails = TransactionDetails.builder()
                .billingAddress("Test")
                .city("Test")
                .firstName("JIAMING")
                .lastName("SHI")
                .postalCode("00000")
                .build();
        TransactionRecordEntity transactionRecord1 = TransactionRecordEntity.builder()
                .transactionDetails(transactionDetails)
                .amount(new BigDecimal("100"))
                .purpose("test")
                .reference("tteesstt1122")
                .timestampTime(LocalDateTime.of(2000,1,1,1,1,1))
                .telephone("+341212121212").build();
        TransactionRecordEntity transactionRecord2 = TransactionRecordEntity.builder()
                .transactionDetails(transactionDetails)
                .amount(new BigDecimal("100"))
                .purpose("test")
                .reference("tteesstt1122")
                .timestampTime(LocalDateTime.of(2000,1,1,1,1,1))
                .telephone("+341212121212").build();
        TransactionRecordEntity transactionRecord3 = TransactionRecordEntity.builder()
                .transactionDetails(transactionDetails)
                .amount(new BigDecimal("200"))
                .purpose("test")
                .reference("tteesstt1122")
                .timestampTime(LocalDateTime.of(2000,1,1,1,1,1))
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
        TransactionRecordEntity transactionRecord1 = TransactionRecordEntity.builder()
                .transactionDetails(transactionDetails)
                .amount(new BigDecimal("100"))
                .purpose("test")
                .reference("tteesstt1122")
                .timestampTime(LocalDateTime.of(2000,1,1,1,1,1))
                .telephone("+341212121212").build();
        TransactionRecordEntity transactionRecord2 = TransactionRecordEntity.builder()
                .transactionDetails(transactionDetails)
                .amount(new BigDecimal("100"))
                .purpose("test")
                .reference("tteesstt1122")
                .timestampTime(LocalDateTime.of(2000,1,1,1,1,1))
                .telephone("+341212121212").build();
        TransactionRecordEntity transactionRecord3 = TransactionRecordEntity.builder()
                .transactionDetails(transactionDetails)
                .amount(new BigDecimal("200"))
                .purpose("test")
                .reference("tteesstt1122")
                .timestampTime(LocalDateTime.of(2000,1,1,1,1,1))
                .telephone("+341212121212").build();
        assertEquals(transactionRecord1.hashCode(),transactionRecord2.hashCode());
        assertNotEquals(transactionRecord1.hashCode(),transactionRecord3.hashCode());
    }

}
