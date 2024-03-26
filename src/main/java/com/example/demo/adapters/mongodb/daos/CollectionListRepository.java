package com.example.demo.adapters.mongodb.daos;

import com.example.demo.adapters.mongodb.entities.CollectionListEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CollectionListRepository extends MongoRepository<CollectionListEntity,String>{
    Optional<CollectionListEntity> readByTelephone(String telephone);
}