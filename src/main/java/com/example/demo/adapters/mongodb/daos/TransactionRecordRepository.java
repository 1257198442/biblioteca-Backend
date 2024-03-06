package com.example.demo.adapters.mongodb.daos;

import com.example.demo.adapters.mongodb.entities.TransactionRecordEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRecordRepository extends MongoRepository<TransactionRecordEntity,String> {
    Optional<TransactionRecordEntity> findByReference(String reference);
    List<TransactionRecordEntity> findByTelephone(String telephone);
}
