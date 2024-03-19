package com.example.demo.adapters.mongodb.daos;

import com.example.demo.adapters.mongodb.entities.AuthorEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends MongoRepository<AuthorEntity,String> {
    Optional<AuthorEntity> readByAuthorId(String authorId);
    Optional<AuthorEntity> deleteByAuthorId(String authorId);

}
