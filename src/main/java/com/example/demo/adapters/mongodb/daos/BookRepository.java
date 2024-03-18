package com.example.demo.adapters.mongodb.daos;

import com.example.demo.adapters.mongodb.entities.BookEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BookRepository extends MongoRepository<BookEntity,String> {
    Optional<BookEntity> readByBookID(String bookId);

}
