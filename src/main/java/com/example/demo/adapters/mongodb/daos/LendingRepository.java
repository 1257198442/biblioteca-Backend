package com.example.demo.adapters.mongodb.daos;

import com.example.demo.adapters.mongodb.entities.LendingEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface LendingRepository extends MongoRepository<LendingEntity,String> {
    Optional<LendingEntity> readByReference(String reference);
    Optional<LendingEntity> deleteByReference(String reference);
    List<LendingEntity> readByUser_Telephone(String telephone);
}
