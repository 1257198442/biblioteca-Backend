package com.example.demo.adapters.rest.dto;

import com.example.demo.TestConfig;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@TestConfig
public class UserUpdateDtoTests {
    @Test
    void userUpdateDto(){
        UserUpdateDto dto1 = UserUpdateDto.builder()
                .name("test")
                .birthdays(LocalDate.of(2000,1,1))
                .description("test")
                .email("test@test.com")
                .build();

        UserUpdateDto dto2 = UserUpdateDto.builder()
                .name("test")
                .birthdays(LocalDate.of(2000,1,1))
                .description("test")
                .email("test@test.com")
                .build();

        UserUpdateDto dto3 = UserUpdateDto.builder()
                .name("test2")
                .birthdays(LocalDate.of(2000,1,1))
                .description("test")
                .email("test@test.com")
                .build();

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
    }
    @Test
    void testHashCode() {
        UserUpdateDto dto1 = UserUpdateDto.builder()
                .name("test")
                .birthdays(LocalDate.of(2000,1,1))
                .description("test")
                .email("test@test.com")
                .build();

        UserUpdateDto dto2 = UserUpdateDto.builder()
                .name("test")
                .birthdays(LocalDate.of(2000,1,1))
                .description("test")
                .email("test@test.com")
                .build();

        UserUpdateDto dto3 = UserUpdateDto.builder()
                .name("test2")
                .birthdays(LocalDate.of(2000,1,1))
                .description("test")
                .email("test@test.com")
                .build();

        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }
}
