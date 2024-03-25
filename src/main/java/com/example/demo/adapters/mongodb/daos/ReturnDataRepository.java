package com.example.demo.adapters.mongodb.daos;


import com.example.demo.adapters.mongodb.entities.ReturnDataEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReturnDataRepository extends MongoRepository<ReturnDataEntity,String> {
    Optional<ReturnDataEntity> readByReference(String reference);
    List<ReturnDataEntity> readByUser_Telephone(String telephone);
    List<ReturnDataEntity> readByBook_BookID(String telephone);
}
