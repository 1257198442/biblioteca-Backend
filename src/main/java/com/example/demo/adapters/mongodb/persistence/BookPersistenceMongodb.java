package com.example.demo.adapters.mongodb.persistence;

import com.example.demo.adapters.mongodb.daos.BookRepository;
import com.example.demo.adapters.mongodb.entities.BookEntity;
import com.example.demo.domain.exceptions.NotFoundException;
import com.example.demo.domain.models.Book;
import com.example.demo.domain.persistence.BookPersistence;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BookPersistenceMongodb implements BookPersistence {
    private final BookRepository bookDao;

    @Autowired
    public BookPersistenceMongodb(BookRepository bookDao){
        this.bookDao = bookDao;
    }
    @Override
    public Book create(Book book) {
       BookEntity bookEntity = new BookEntity();
       BeanUtils.copyProperties(book,bookEntity);
        return this.bookDao
                .save(bookEntity)
                .toBook();
    }

    @Override
    public Book read(String bookId) {
        return this.bookDao.readByBookID(bookId)
                .orElseThrow(()->new NotFoundException("BookId: "+bookId+" is not Fount"))
                .toBook();
    }

    @Override
    public List<Book> readAll(){
        return this.bookDao.findAll()
                .stream()
                .map(BookEntity::toBook)
                .collect(Collectors.toList());
    }

}