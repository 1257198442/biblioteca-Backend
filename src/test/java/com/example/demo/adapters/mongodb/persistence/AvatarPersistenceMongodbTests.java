package com.example.demo.adapters.mongodb.persistence;

import com.example.demo.TestConfig;
import com.example.demo.adapters.mongodb.entities.AvatarEntity;
import com.example.demo.domain.models.Avatar;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestConfig
public class AvatarPersistenceMongodbTests {
    @Autowired
    private AvatarPersistenceMongodb avatarPersistenceMongodb;

    @Test
    void testRead(){
        Avatar avatar = avatarPersistenceMongodb.read("+34666");
        assertEquals("user.png",avatar.getFileName());
        assertEquals("https://localhost/images/avatar/user.png",avatar.getUrl());
        assertEquals("+34666",avatar.getTelephone());
        assertEquals(LocalDateTime.of(2000,1,1,1,1,1),avatar.getUploadTime());
    }

    @Test
    void testUpdate(){
        Avatar avatar = avatarPersistenceMongodb.read("+34666");
        avatar.setFileName("test.png");
        avatarPersistenceMongodb.update(avatar);
        Avatar avatar1 = avatarPersistenceMongodb.read("+34666");
        assertEquals("test.png",avatar1.getFileName());
        avatar.setFileName("user.png");
        avatarPersistenceMongodb.update(avatar);
    }
}
