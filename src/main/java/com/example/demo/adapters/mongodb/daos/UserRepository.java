package com.example.demo.adapters.mongodb.daos;


import com.example.demo.adapters.mongodb.entities.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserEntity,String> {
    Optional<UserEntity> readByTelephone(String telephone);

}
