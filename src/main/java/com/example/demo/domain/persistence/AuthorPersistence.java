package com.example.demo.domain.persistence;

import com.example.demo.domain.models.Author;

import java.util.List;

public interface AuthorPersistence {
    Author create(Author author);
    Author read(String authorId);
    List<Author> readAll();
    Author getAuthorData(String authorId);

}