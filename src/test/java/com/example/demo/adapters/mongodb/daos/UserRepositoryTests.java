package com.example.demo.adapters.mongodb.daos;

import com.example.demo.TestConfig;
import com.example.demo.adapters.mongodb.entities.UserEntity;
import com.example.demo.domain.exceptions.NotFoundException;
import com.example.demo.domain.models.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
class UserRepositoryTests {
    @Autowired
    private  UserRepository userRepository;

    @Test
    void testRead() {
        assertTrue(this.userRepository.readByTelephone("+34645321068").isPresent());
        assertFalse(this.userRepository.readByTelephone("+34999999999").isPresent());

    }
    @Test
    void testCreat(){
        UserEntity userEntity = UserEntity.builder()
                .name("Test")
                .email("test@test.com")
                .password("6")
                .telephone("+34000000000")
                .createTime(LocalDateTime.of(2024,2,15, 0, 0, 0))
                .role(Role.CLIENT)
                .active(true)
                .build();
        this.userRepository.save(userEntity);
        UserEntity userEntity1 = userRepository.readByTelephone(userEntity.getTelephone()).orElseThrow(()->new NotFoundException("User telephone: "+userEntity.getTelephone()+" is not Fount"));
        assertEquals(userEntity,userEntity1);
    }

}
