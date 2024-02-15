package com.example.demo.adapters.mongodb.entites;

import com.example.demo.TestConfig;
import com.example.demo.adapters.mongodb.entities.UserEntity;
import com.example.demo.domain.models.Admin;
import com.example.demo.domain.models.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestConfig
public class UserEntityIT {
    @Test
    void testToUser(){
        UserEntity userEntity = UserEntity.builder()
                .name("test")
                .telephone("+341111111111")
                .email("test@test.com")
                .admin(Admin.CLIENT)
                .password("6")
                .active(true)
                .createTime(LocalDateTime.of(2025,2,15,0,0,0)).build();
        User user = User.builder()
                .name("test")
                .telephone("+341111111111")
                .email("test@test.com")
                .admin(Admin.CLIENT)
                .password("6")
                .active(true)
                .createTime(LocalDateTime.of(2025,2,15,0,0,0))
                .build();
        assertEquals(userEntity.toUser(),user);
    }
}
