package com.example.demo.adapters.mongodb.daos;

import com.example.demo.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfig
public class TransactionRecordRepositoryTests {
    @Autowired
    private TransactionRecordRepository transactionRecordRepository;
    @Test
    void testReadByTelephone(){
        assertTrue(!transactionRecordRepository.findByTelephone("+34123").isEmpty());
        assertTrue(transactionRecordRepository.findByTelephone("null").isEmpty());
    }
    @Test
    void testReadByReference(){
        assertTrue(transactionRecordRepository.findByReference("ijadlkfjsjf1").isPresent());
        assertTrue(transactionRecordRepository.findByReference("null").isEmpty());
    }
}
