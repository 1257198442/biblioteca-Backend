package com.example.demo.adapters.rest.dto;

import com.example.demo.TestConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@TestConfig
public class UserUploadDtoTests {
    @Test
    void userUploadDto(){
        UserUploadDto dto1 = UserUploadDto.builder()
                .name("test")
                .password("6")
                .telephone("+1234567890")
                .email("test@test.com")
                .build();

        UserUploadDto dto2 = UserUploadDto.builder()
                .name("test")
                .password("6")
                .telephone("+1234567890")
                .email("test@test.com")
                .build();

        UserUploadDto dto3 = UserUploadDto.builder()
                .name("test2")
                .password("6")
                .telephone("+0987654321")
                .email("test@test.com")
                .build();

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
    }

    @Test
    void testHashCode() {
        UserUploadDto dto1 = UserUploadDto.builder()
                .name("test")
                .password("6")
                .telephone("+1234567890")
                .email("test@test.com")
                .build();

        UserUploadDto dto2 = UserUploadDto.builder()
                .name("test")
                .password("6")
                .telephone("+1234567890")
                .email("test@test.com")
                .build();

        UserUploadDto dto3 = UserUploadDto.builder()
                .name("test2")
                .password("6")
                .telephone("+0987654321")
                .email("test@test.com")
                .build();

        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }
}
