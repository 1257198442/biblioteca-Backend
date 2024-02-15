package com.example.demo.domain.models;

import com.example.demo.TestConfig;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@TestConfig
public class UserIT {
    @Test
    void testToShow(){
        User user = User.builder()
                .name("test")
                .password("6")
                .createTime(LocalDateTime.of(2024,2,16,0,0,0))
                .telephone("+349996666666")
                .email("test@test.com")
                .admin(Admin.CLIENT)
                .active(true)
                .build();
        User userToShow = user.toShow();
        assertEquals("test",userToShow.getName());
        assertNotEquals("6",userToShow.getPassword());
        assertEquals(LocalDateTime.of(2024,2,16,0,0,0),userToShow.getCreateTime());

    }
}
