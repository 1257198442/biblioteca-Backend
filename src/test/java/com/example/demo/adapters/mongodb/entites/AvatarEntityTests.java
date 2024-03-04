package com.example.demo.adapters.mongodb.entites;

import com.example.demo.TestConfig;
import com.example.demo.adapters.mongodb.entities.AvatarEntity;
import com.example.demo.domain.models.Avatar;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
public class AvatarEntityTests {
    @Test
    void testToAvatarAndAvatarEntity(){
        AvatarEntity avatarEntity = AvatarEntity.builder()
                .uploadTime(LocalDateTime.of(2000, 1, 1, 1, 1))
                .telephone("+34111111")
                .url("test")
                .fileName("test").build();
        Avatar avatar = Avatar.builder()
                .uploadTime(LocalDateTime.of(2000, 1, 1, 1, 1))
                .telephone("+34111111")
                .url("test")
                .fileName("test").build();
        assertEquals(avatar,avatarEntity.toAvatar());
        assertEquals(avatarEntity,new AvatarEntity(avatar));
    }

    @Test
    void testEquals(){
        AvatarEntity avatarEntity1 = AvatarEntity.builder()
                .uploadTime(LocalDateTime.of(2000, 1, 1, 1, 1))
                .telephone("+34111111")
                .url("test")
                .fileName("test").build();
        AvatarEntity avatarEntity2 = AvatarEntity.builder()
                .uploadTime(LocalDateTime.of(2000, 1, 1, 1, 1))
                .telephone("+34222222")
                .url("test")
                .fileName("test").build();
        AvatarEntity avatarEntity3 = AvatarEntity.builder()
                .uploadTime(LocalDateTime.of(2000, 1, 1, 1, 1))
                .telephone("+34111111")
                .url("test")
                .fileName("test").build();
        assertEquals(avatarEntity1,avatarEntity3);
        assertNotEquals(avatarEntity1,avatarEntity2);
    }

    @Test
    void testHashCode(){
        AvatarEntity avatarEntity1 = AvatarEntity.builder()
                .uploadTime(LocalDateTime.of(2000, 1, 1, 1, 1))
                .telephone("+34111111")
                .url("test")
                .fileName("test").build();
        AvatarEntity avatarEntity2 = AvatarEntity.builder()
                .uploadTime(LocalDateTime.of(2000, 1, 1, 1, 1))
                .telephone("+34222222")
                .url("test")
                .fileName("test").build();
        AvatarEntity avatarEntity3 = AvatarEntity.builder()
                .uploadTime(LocalDateTime.of(2000, 1, 1, 1, 1))
                .telephone("+34111111")
                .url("test")
                .fileName("test").build();
        assertEquals(avatarEntity1.hashCode(),avatarEntity3.hashCode());
        assertNotEquals(avatarEntity1.hashCode(),avatarEntity2.hashCode());
    }

}
