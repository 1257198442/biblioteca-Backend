package com.example.demo.adapters.mongodb.daos;


import com.example.demo.adapters.mongodb.entities.LibraryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LibraryRepository extends MongoRepository<LibraryEntity,String> {
    Optional<LibraryEntity> readByName(String name);
}
