package com.example.demo.adapters.mongodb.persistence;

import com.example.demo.TestConfig;
import com.example.demo.domain.exceptions.NotFoundException;
import com.example.demo.domain.models.Role;
import com.example.demo.domain.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
public class UserPersistenceMongodbTests {
    @Autowired
    private UserPersistenceMongodb userPersistenceMongodb;
    @Test
    void testRead(){
        User user = this.userPersistenceMongodb.read("666666666");
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

}
