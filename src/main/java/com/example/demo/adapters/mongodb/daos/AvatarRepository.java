package com.example.demo.adapters.mongodb.daos;

import com.example.demo.adapters.mongodb.entities.AvatarEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AvatarRepository extends MongoRepository<AvatarEntity,String> {
    Optional<AvatarEntity> readByTelephone(String telephone);
}