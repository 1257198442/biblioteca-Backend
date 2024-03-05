package com.example.demo.adapters.mongodb.daos;

import com.example.demo.adapters.mongodb.entities.TransactionRecordEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRecordDao extends MongoRepository<TransactionRecordEntity,String> {

}
