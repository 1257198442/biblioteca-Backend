package com.example.demo.adapters.mongodb.daos;

import com.example.demo.adapters.mongodb.entities.LendingDataEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface LendingDataRepository extends MongoRepository<LendingDataEntity,String> {
    Optional<LendingDataEntity> readByReference(String reference);
    List<LendingDataEntity> readByUser_Telephone(String telephone);
}
