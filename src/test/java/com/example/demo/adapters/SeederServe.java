package com.example.demo.adapters;

import com.example.demo.adapters.mongodb.daos.UserRepository;
import com.example.demo.adapters.mongodb.entities.UserEntity;
import com.example.demo.domain.models.Admin;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service
public class SeederServe {
    @Autowired
    private UserRepository userRepository;

    public void seedDatabase() {
        LogManager.getLogger(this.getClass()).warn("------- database Initial Load -----------");
        String Password="6";
        UserEntity[] userEntities = {
                new UserEntity("user1", Password, LocalDateTime.now(),"+34645321068", "user1@example.com", Admin.ROOT,true),
                new UserEntity("user2", Password, LocalDateTime.now(),"+34666666666", "user2@example.com", Admin.ADMINISTRATOR,true),
                new UserEntity("user3", Password, LocalDateTime.now(),"+34666000001", "user3@example.com", Admin.CLIENT,true),
                new UserEntity("user4", Password, LocalDateTime.now(),"+34666000002", "user4@example.com", Admin.BAN,false)
        };
        this.userRepository.saveAll(Arrays.asList(userEntities));
    }

    public void deleteAll() {
        this.userRepository.deleteAll();
    }
}
