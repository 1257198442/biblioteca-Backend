package com.example.demo.adapters.mongodb.daos;


import com.example.demo.adapters.mongodb.entities.TypeEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TypeRepository extends MongoRepository<TypeEntity,String> {
    Optional<TypeEntity> readByName(String name);

}