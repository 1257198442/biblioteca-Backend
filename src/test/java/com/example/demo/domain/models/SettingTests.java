package com.example.demo.domain.models;

import com.example.demo.TestConfig;
import com.example.demo.adapters.rest.dto.SettingUpdateDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@TestConfig
public class SettingTests {
    @Test
    void testSettingUpdateDto(){
        Setting dto1 = Setting.builder().hideMyProfile(false).build();
        Setting dto2 = Setting.builder().hideMyProfile(true).build();
        Setting dto3 = Setting.builder().hideMyProfile(false).build();

        assertNotEquals(dto1, dto2);
        assertEquals(dto1, dto3);
    }

    @Test
    void testHashCode() {
        Setting dto1 = Setting.builder().hideMyProfile(false).build();
        Setting dto2 = Setting.builder().hideMyProfile(true).build();
        Setting dto3 = Setting.builder().hideMyProfile(false).build();
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
        assertEquals(dto1.hashCode(), dto3.hashCode());
    }
}
