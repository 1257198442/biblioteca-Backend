package com.example.demo.adapters.mongodb.daos;

import com.example.demo.adapters.mongodb.entities.WalletEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WalletRepository extends MongoRepository<WalletEntity,String> {
    Optional<WalletEntity> findByTelephone(String telephone);
}
