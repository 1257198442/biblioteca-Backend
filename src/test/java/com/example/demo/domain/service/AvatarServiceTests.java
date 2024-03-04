package com.example.demo.domain.service;

import com.example.demo.TestConfig;
import com.example.demo.adapters.mongodb.entities.AvatarEntity;
import com.example.demo.domain.exceptions.NotFoundException;
import com.example.demo.domain.models.Avatar;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
public class AvatarServiceTests {
    @Autowired
    private AvatarService avatarService;

    @Test
    void testRead(){
       Avatar avatar = avatarService.read("+34645321068");
       //404
       assertThrows(NotFoundException.class,()->{
           avatarService.read("null");
       });
       assertEquals("user.png",avatar.getFileName());
       assertEquals("https://localhost/images/avatar/user.png",avatar.getUrl());
       assertEquals("+34645321068",avatar.getTelephone());
    }

    @Test
    void testUpdate(){
        Avatar avatar = avatarService.read("+34123");
        avatar.setFileName("test");
        avatarService.update(avatar);
        assertEquals("test",avatarService.read("+34123").getFileName());
        avatar.setFileName("user.png");
        avatarService.update(avatar);
    }
}
