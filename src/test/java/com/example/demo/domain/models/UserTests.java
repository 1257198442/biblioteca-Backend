package com.example.demo.domain.models;

import com.example.demo.TestConfig;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@TestConfig
public class UserTests {
    @Test
    void testToShow(){
        LocalDateTime createTimeTest = LocalDateTime.of(2024,2,16,0,0,0);
        LocalDate birthdaysTimeTest = LocalDate.of(2025, 2, 15);
        User user = User.builder()
                .name("test")
                .password("6")
                .createTime(createTimeTest)
                .telephone("+349996666666")
                .email("test@test.com")
                .role(Role.CLIENT)
                .active(true)
                .description("test")
                .birthdays(birthdaysTimeTest)
                .build();
        User userToShow = user.toShow();
        assertEquals("test",userToShow.getName());
        assertNotEquals("6",userToShow.getPassword());
        assertEquals(createTimeTest,userToShow.getCreateTime());

    }
    @Test
    void testEquals() {
        User user1 = User.builder()
                .name("test")
                .telephone("+341111111111")
                .email("test@test.com")
                .role(Role.CLIENT)
                .password("6")
                .active(true)
                .createTime(LocalDateTime.of(2025,2,15,0,0,0))
                .description("test")
                .birthdays(LocalDate.of(2025, 2, 15))
                .build();

        User user2 = User.builder()
                .name("test")
                .telephone("+341111111111")
                .email("test@test.com")
                .role(Role.CLIENT)
                .password("6")
                .active(true)
                .createTime(LocalDateTime.of(2025,2,15,0,0,0))
                .description("test")
                .birthdays(LocalDate.of(2025, 2, 15))
                .build();

        User user3 = User.builder()
                .name("test2")
                .telephone("+341111111111")
                .email("test@test.com")
                .role(Role.CLIENT)
                .password("6")
                .active(true)
                .createTime(LocalDateTime.of(2025,2,15,0,0,0))
                .description("test")
                .birthdays(LocalDate.of(2025, 2, 15))
                .build();

        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
    }

    @Test
    void testHashCode() {
        User user1 = User.builder()
                .name("test")
                .telephone("+341111111111")
                .email("test@test.com")
                .role(Role.CLIENT)
                .password("6")
                .active(true)
                .createTime(LocalDateTime.of(2025,2,15,0,0,0))
                .description("test")
                .birthdays(LocalDate.of(2025, 2, 15))
                .build();

        User user2 = User.builder()
                .name("test")
                .telephone("+341111111111")
                .email("test@test.com")
                .role(Role.CLIENT)
                .password("6")
                .active(true)
                .createTime(LocalDateTime.of(2025,2,15,0,0,0))
                .description("test")
                .birthdays(LocalDate.of(2025, 2, 15))
                .build();

        User user3 = User.builder()
                .name("test2")
                .telephone("+341111111111")
                .email("test@test.com")
                .role(Role.CLIENT)
                .password("6")
                .active(true)
                .createTime(LocalDateTime.of(2025,2,15,0,0,0))
                .description("test")
                .birthdays(LocalDate.of(2025, 2, 15))
                .build();

        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1.hashCode(), user3.hashCode());
    }
}
