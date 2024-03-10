package com.example.demo.adapters.rest.dto;

import com.example.demo.TestConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@TestConfig
public class PasswordUpdateDtoTests {
    @Test
    void testPasswordUpdateDto(){
        PasswordUpdateDto dto1 = PasswordUpdateDto.builder().oldPassword("6").newPassword("7").build();
        PasswordUpdateDto dto2 = PasswordUpdateDto.builder().oldPassword("7").newPassword("6").build();
        PasswordUpdateDto dto3 = PasswordUpdateDto.builder().oldPassword("6").newPassword("7").build();


        assertNotEquals(dto1, dto2);
        assertEquals(dto1, dto3);
    }

    @Test
    void testHashCode() {
        PasswordUpdateDto dto1 = PasswordUpdateDto.builder().oldPassword("6").newPassword("7").build();
        PasswordUpdateDto dto2 = PasswordUpdateDto.builder().oldPassword("7").newPassword("6").build();
        PasswordUpdateDto dto3 = PasswordUpdateDto.builder().oldPassword("6").newPassword("7").build();
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
        assertEquals(dto1.hashCode(), dto3.hashCode());
    }
}
