package com.example.demo.domain.models;

import com.example.demo.TestConfig;
import com.example.demo.adapters.mongodb.entities.AvatarEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@TestConfig
public class AvatarTests {
    @Test
    void testEquals(){
        Avatar avatar1 = Avatar.builder()
                .uploadTime(LocalDateTime.of(2000, 1, 1, 1, 1))
                .telephone("+34111111")
                .url("test")
                .fileName("test").build();
        Avatar avatar2 = Avatar.builder()
                .uploadTime(LocalDateTime.of(2000, 1, 1, 1, 1))
                .telephone("+34222222")
                .url("test")
                .fileName("test").build();
        Avatar avatar3 = Avatar.builder()
                .uploadTime(LocalDateTime.of(2000, 1, 1, 1, 1))
                .telephone("+34111111")
                .url("test")
                .fileName("test").build();
        assertEquals(avatar1,avatar3);
        assertNotEquals(avatar1,avatar2);
    }

    @Test
    void testHashCode(){
        Avatar avatar1 = Avatar.builder()
                .uploadTime(LocalDateTime.of(2000, 1, 1, 1, 1))
                .telephone("+34111111")
                .url("test")
                .fileName("test").build();
        Avatar avatar2 = Avatar.builder()
                .uploadTime(LocalDateTime.of(2000, 1, 1, 1, 1))
                .telephone("+34222222")
                .url("test")
                .fileName("test").build();
        Avatar avatar3 = Avatar.builder()
                .uploadTime(LocalDateTime.of(2000, 1, 1, 1, 1))
                .telephone("+34111111")
                .url("test")
                .fileName("test").build();
        assertEquals(avatar1.hashCode(),avatar3.hashCode());
        assertNotEquals(avatar1.hashCode(),avatar2.hashCode());
    }
}
