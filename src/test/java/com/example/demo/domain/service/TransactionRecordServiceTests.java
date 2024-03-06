package com.example.demo.domain.service;

import com.example.demo.TestConfig;
import com.example.demo.adapters.rest.dto.TransactionRecordDto;
import com.example.demo.domain.models.TransactionDetails;
import com.example.demo.domain.models.TransactionRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfig
public class TransactionRecordServiceTests {
    @Autowired
    private TransactionRecordService transactionRecordService;
    @Test
    void testCreateAndRead(){
        TransactionDetails transactionDetails = TransactionDetails.builder()
                .billingAddress("test")
                .city("test")
                .firstName("test")
                .lastName("test")
                .postalCode("11111").build();
        TransactionRecordDto transactionRecordDto = TransactionRecordDto.builder()
                .telephone("+34666000002")
                .amount(new BigDecimal("100"))
                .transactionDetails(transactionDetails)
                .purpose("test")
                .build();
        TransactionRecord transactionRecord = transactionRecordService.create(transactionRecordDto);
        assertThat(transactionRecordService.readByReference(transactionRecord.getReference())).isNotNull();
        assertFalse(transactionRecordService.readByTelephone(transactionRecord.getTelephone()).isEmpty());

    }
}
