package com.example.demo.adapters.mongodb.entites;

import com.example.demo.TestConfig;
import com.example.demo.adapters.mongodb.entities.UserEntity;
import com.example.demo.domain.models.Role;
import com.example.demo.domain.models.Setting;
import com.example.demo.domain.models.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@TestConfig
public class UserEntityTests {
    @Test
    void testToUser(){
        Setting setting = Setting.builder().hideMyProfile(true).emailWhenOrderIsGenerated(true).build();
        UserEntity userEntity = UserEntity.builder()
                .name("test")
                .telephone("+341111111111")
                .email("test@test.com")
                .role(Role.CLIENT)
                .password("6")
                .active(true)
                .createTime(LocalDateTime.of(2025,2,15,0,0,0))
                .description("test")
                .birthdays(LocalDate.of(2025, 2, 15))
                .setting(setting)
                .build();
        User user = User.builder()
                .name("test")
                .telephone("+341111111111")
                .email("test@test.com")
                .role(Role.CLIENT)
                .password("6")
                .active(true)
                .createTime(LocalDateTime.of(2025,2,15,0,0,0))
                .description("test")
                .birthdays(LocalDate.of(2025, 2, 15))
                .setting(setting)
                .build();

        assertEquals(userEntity.toUser(),user);
        assertEquals(new UserEntity(user).toUser(),user);
    }

    @Test
    void testEquals() {
        Setting setting = Setting.builder().hideMyProfile(true).build();
        UserEntity user1 = UserEntity.builder()
                .name("test")
                .telephone("+341111111111")
                .email("test@test.com")
                .role(Role.CLIENT)
                .password("6")
                .active(true)
                .createTime(LocalDateTime.of(2025,2,15,0,0,0))
                .description("test")
                .birthdays(LocalDate.of(2025, 2, 15))
                .setting(setting)
                .build();

        UserEntity user2 = UserEntity.builder()
                .name("test")
                .telephone("+341111111111")
                .email("test@test.com")
                .role(Role.CLIENT)
                .password("6")
                .active(true)
                .createTime(LocalDateTime.of(2025,2,15,0,0,0))
                .description("test")
                .birthdays(LocalDate.of(2025, 2, 15))
                .setting(setting)
                .build();

        UserEntity user3 = UserEntity.builder()
                .name("test2")
                .telephone("+341111111111")
                .email("test@test.com")
                .role(Role.CLIENT)
                .password("6")
                .active(true)
                .createTime(LocalDateTime.of(2025,2,15,0,0,0))
                .description("test")
                .birthdays(LocalDate.of(2025, 2, 15))
                .setting(setting)
                .build();

        assertEquals(user1, user2, "user1 should be equal to user2");
        assertNotEquals(user1, user3, "user1 should not be equal to user3");
    }

    @Test
    void testHashCode() {
        Setting setting = Setting.builder().hideMyProfile(true).emailWhenOrderIsGenerated(true).build();
        UserEntity user1 = UserEntity.builder()
                .name("test")
                .telephone("+341111111111")
                .email("test@test.com")
                .role(Role.CLIENT)
                .password("6")
                .active(true)
                .createTime(LocalDateTime.of(2025,2,15,0,0,0))
                .description("test")
                .birthdays(LocalDate.of(2025, 2, 15))
                .setting(setting)
                .build();

        UserEntity user2 = UserEntity.builder()
                .name("test")
                .telephone("+341111111111")
                .email("test@test.com")
                .role(Role.CLIENT)
                .password("6")
                .active(true)
                .createTime(LocalDateTime.of(2025,2,15,0,0,0))
                .description("test")
                .birthdays(LocalDate.of(2025, 2, 15))
                .setting(setting)
                .build();

        UserEntity user3 = UserEntity.builder()
                .name("test2")
                .telephone("+341111111111")
                .email("test@test.com")
                .role(Role.CLIENT)
                .password("6")
                .active(true)
                .createTime(LocalDateTime.of(2025,2,15,0,0,0))
                .description("test")
                .birthdays(LocalDate.of(2025, 2, 15))
                .setting(setting)
                .build();

        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1.hashCode(), user3.hashCode());
    }

}
