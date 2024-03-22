package com.example.demo.domain.service;

import com.example.demo.TestConfig;
import com.example.demo.domain.models.LendingData;
import com.example.demo.domain.models.ReturnData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestConfig
public class ReturnDataServiceTests {
    @Autowired
    private ReturnDataService returnDataService;
    @Autowired
    private LendingDataService lendingService;
    @Test
    void testCreateAndRead(){
        String reference = returnDataService.create("4").getReference();
        LendingData lending = lendingService.read("4");
        ReturnData restitution = returnDataService.read(reference);
        assertEquals(lending.getLendingTime(),restitution.getLendingTime());
        assertEquals(lending.getBook(),restitution.getBook());
        assertEquals(lending.getUser(),restitution.getUser());
    }
    @Test
    void testReadAll(){
        assertNotNull(returnDataService.readAll());
    }
}
