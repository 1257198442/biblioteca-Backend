package com.example.demo.adapters.rest.dto;

import com.example.demo.TestConfig;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@TestConfig
public class SettingUpdateDtoTests {
    @Test
    void testSettingUpdateDto(){
        SettingUpdateDto dto1 = SettingUpdateDto.builder().hideMyProfile(false).emailWhenOrderIsGenerated(true).build();
        SettingUpdateDto dto2 = SettingUpdateDto.builder().hideMyProfile(true).emailWhenOrderIsGenerated(true).build();
        SettingUpdateDto dto3 = SettingUpdateDto.builder().hideMyProfile(false).emailWhenOrderIsGenerated(true).build();
        assertNotEquals(dto1, dto2);
        assertEquals(dto1, dto3);
    }

    @Test
    void testHashCode() {
        SettingUpdateDto dto1 = SettingUpdateDto.builder().hideMyProfile(false).emailWhenOrderIsGenerated(true).build();
        SettingUpdateDto dto2 = SettingUpdateDto.builder().hideMyProfile(true).emailWhenOrderIsGenerated(true).build();
        SettingUpdateDto dto3 = SettingUpdateDto.builder().hideMyProfile(false).emailWhenOrderIsGenerated(true).build();
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
        assertEquals(dto1.hashCode(), dto3.hashCode());
    }
}
