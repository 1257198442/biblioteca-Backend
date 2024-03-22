package com.example.demo.adapters.mongodb.daos;


import com.example.demo.adapters.mongodb.entities.ReturnDataEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ReturnDataRepository extends MongoRepository<ReturnDataEntity,String> {
    Optional<ReturnDataEntity> readByReference(String reference);

}
