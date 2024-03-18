package com.example.demo.adapters.mongodb.persistence;

import com.example.demo.TestConfig;
import com.example.demo.adapters.mongodb.entities.UserEntity;
import com.example.demo.domain.exceptions.NotFoundException;
import com.example.demo.domain.models.Role;
import com.example.demo.domain.models.Setting;
import com.example.demo.domain.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
public class UserPersistenceMongodbTests {
    @Autowired
    private UserPersistenceMongodb userPersistenceMongodb;
    @Test
    void testRead(){
        User user = this.userPersistenceMongodb.read("+34666666666");
        assertEquals("root", user.getName());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        assertTrue(passwordEncoder.matches("6",user.getPassword()));
        assertEquals("1257198442@qq.com",user.getEmail());
        assertEquals(Role.ROOT,user.getRole());
        assertEquals(true,user.getActive());
        assertThrows(NotFoundException.class, () -> {
            userPersistenceMongodb.read("null");
        });
    }
    @Test
    void testUpdate(){
        Setting setting = Setting.builder().hideMyProfile(true).emailWhenOrderIsGenerated(true).build();
        Setting setting1 = Setting.builder().hideMyProfile(false).emailWhenOrderIsGenerated(true).build();
        User user = User.builder()
                .name("Test99")
                .email("test@test.com")
                .password("6")
                .telephone("+34000000000")
                .createTime(LocalDateTime.of(2024,2,15, 0, 0, 0))
                .role(Role.CLIENT)
                .active(true)
                .description("test")
                .birthdays(LocalDate.of(2000,1,1))
                .setting(setting)
                .build();
        User user1 = User.builder()
                .name("Test999")
                .email("test@test.com")
                .password("6")
                .telephone("+34000000000")
                .createTime(LocalDateTime.of(2024,2,15, 0, 0, 0))
                .role(Role.CLIENT)
                .active(true)
                .description("test1")
                .birthdays(LocalDate.of(2000,10,1))
                .setting(setting1)
                .build();
        User user2 = User.builder()
                .name("Test999")
                .email("test@test.com")
                .password("6")
                .telephone("null")
                .createTime(LocalDateTime.of(2024,2,15, 0, 0, 0))
                .role(Role.CLIENT)
                .active(true)
                .description("test")
                .birthdays(LocalDate.of(2000,1,1))
                .setting(setting)
                .build();
        this.userPersistenceMongodb.create(user);

        assertEquals(user1,this.userPersistenceMongodb.update(user1));
        assertThrows(NotFoundException.class,()->{
            this.userPersistenceMongodb.update(user2);
        });
    }
    @Test
    void testReadAll(){
        List<User> userList =this.userPersistenceMongodb.readAll();
        assertNotNull(userList);
    }
    @Test
    void testCreate(){
        Setting setting = Setting.builder().hideMyProfile(true).emailWhenOrderIsGenerated(true).build();
        User user = User.builder()
                .name("test")
                .password("6")
                .createTime(LocalDateTime.of(2000,1,1,1,1,1))
                .telephone("+34999")
                .email("test@test.com")
                .role(Role.CLIENT)
                .active(true)
                .description("test")
                .birthdays(LocalDate.of(2000,1,1))
                .setting(setting).build();
        userPersistenceMongodb.create(user);
        User userResult = userPersistenceMongodb.read(user.getTelephone());
        assertEquals(userResult,user);

    }
}
