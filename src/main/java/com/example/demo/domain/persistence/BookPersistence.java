package com.example.demo.domain.persistence;

import com.example.demo.domain.models.Book;

import java.util.List;

public interface BookPersistence {
    Book create(Book book);
    Book read(String bookId);
    List<Book> readAll();
}