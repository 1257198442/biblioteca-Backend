package com.example.demo.adapters.mongodb.daos;

import com.example.demo.adapters.mongodb.entities.BookEntity;
import com.example.demo.domain.models.Language;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends MongoRepository<BookEntity,String> {
    Optional<BookEntity> readByBookID(String bookId);
    Optional<BookEntity> deleteByBookID(String bookId);
    @Query("{$and:[" +
            "?#{ [0] == null ? {_id : {$ne:null}} : { publisher : {$regex:[0], $options: 'i'} } }, " +
            "?#{ [1] == null ? {_id : {$ne:null}} : { authorId : [1] } }, " +
            "?#{ [2] == null ? {_id : {$ne:null}} : { name : {$regex:[2], $options: 'i'} } }, " +
            "?#{ [3] == null ? {_id : {$ne:null}} : { language : [3].toString() } } " +
            "?#{ [4] == null ? {_id : {$ne:null}} : { barcode : [4] } } " +
            "?#{ [5] == null ? {_id : {$ne:null}} : { issn : [5] } } " +
            "?#{ [6] == null ? {_id : {$ne:null}} : { isbn : [6] } } " +
            "?#{ [7] == null ? {_id : {$ne:null}} : { bookType : [7] } }, " +
            "] }")
    List<BookEntity> findBooks(String publisher, String authorId, String name, Language language, String barcode, String issn, String isbn,String bookType);

}
