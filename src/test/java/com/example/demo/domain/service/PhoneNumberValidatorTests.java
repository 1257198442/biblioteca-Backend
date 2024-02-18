package com.example.demo.domain.service;

import com.example.demo.TestConfig;
import com.example.demo.domain.exceptions.UnprocessableEntityException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestConfig
public class PhoneNumberValidatorTests {
    @Autowired
    private PhoneNumberValidator phoneNumberValidator;
    @Test
    void testValidate(){
        String telTest1="+34666666666";
        String telTest2="666666666";
        assertNotNull(phoneNumberValidator.validate(telTest1));
        assertThrows(UnprocessableEntityException.class, () -> {
            phoneNumberValidator.validate(telTest2);
        });
    }
}
