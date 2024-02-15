package com.example.demo.adapters.mongodb.persistence;

import com.example.demo.TestConfig;
import com.example.demo.domain.exceptions.NotFoundException;
import com.example.demo.domain.models.Admin;
import com.example.demo.domain.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestConfig
public class UserPersistenceMongodbIT {
    @Autowired
    private UserPersistenceMongodb userPersistenceMongodb;
    @Test
    void testRead(){
        User user = this.userPersistenceMongodb.read("+34645321068");
        assertEquals("user1", user.getName());
        assertEquals("6", user.getPassword());
        assertEquals("user1@example.com",user.getEmail());
        assertEquals(Admin.ROOT,user.getAdmin());
        assertEquals(true,user.getActive());
        assertThrows(NotFoundException.class, () -> {
            userPersistenceMongodb.read("null");
        });
    }

}
