package com.example.demo.adapters.mongodb.persistence;

import com.example.demo.adapters.mongodb.daos.AuthorRepository;
import com.example.demo.adapters.mongodb.daos.BookRepository;
import com.example.demo.adapters.mongodb.entities.AuthorEntity;
import com.example.demo.adapters.mongodb.entities.BookEntity;
import com.example.demo.domain.exceptions.LockedResourceException;
import com.example.demo.domain.exceptions.NotFoundException;
import com.example.demo.domain.models.Book;
import com.example.demo.domain.models.BookStatus;
import com.example.demo.domain.models.Language;
import com.example.demo.domain.persistence.BookPersistence;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class BookPersistenceMongodb implements BookPersistence {
    private final BookRepository bookDao;
    private final AuthorRepository authorDao;
    @Autowired
    public BookPersistenceMongodb(BookRepository bookDao,
                                  AuthorRepository authorDao){
        this.bookDao = bookDao;
        this.authorDao = authorDao;
    }

    @Override
    public Book create(Book book) {
        return this.bookDao
                .save(new BookEntity(book))
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

    @Override
    public Book update(Book book) {
        BookEntity bookEntity = this.bookDao
                .readByBookID(book.getBookID())
                .orElseThrow(()->new NotFoundException("BookId: "+book.getBookID()+" is not Fount"));
        BeanUtils.copyProperties(book,bookEntity);
        return this.bookDao
                .save(bookEntity)
                .toBook();
    }

    @Override
    public Book delete(String bookId) {
        Book book = this.bookDao.readByBookID(bookId)
                .orElseThrow(()->new NotFoundException("BookId: "+bookId+" is not Fount")).toBook();
        if(book.getStatus().equals(BookStatus.OCCUPIED)){
            throw new LockedResourceException("The book "+book.getBookID()+" is on loan");
        }else {
            return this.bookDao.deleteByBookID(bookId)
                    .orElseThrow(()->new NotFoundException("BookId: "+bookId+" is not Fount"))
                    .toBook();
        }
    }

    @Override
    public List<Book> searchBook(String publisher, String authorName, String name, String language, String bookType,String barcode,String issn,String isbn){
        Language language1 = language == null ?null:Language.fromString(language);
        if(authorName!=null){
            return this.authorDao
                    .readByNameIgnoreCaseRegex(".*(?i)"+authorName+".*")
                    .stream()
                    .map(AuthorEntity::getAuthorId)
                    .flatMap(authorId-> getBooksBySearch(publisher,authorId,name,language1,bookType,barcode,issn,isbn))
                    .collect(Collectors.toList());
        }else {
            return this.getBooksBySearch(publisher,null,name,language1,bookType,barcode,issn,isbn)
                    .collect(Collectors.toList());
        }
    }
    @Override
    public List<Book> readByAuthorId(String authorId){
        return this.bookDao.findBooks(null,authorId,null,null,null,null,null,null).stream().map(BookEntity::toBook).collect(Collectors.toList());
    }
    private Stream<Book> getBooksBySearch(String publisher, String authorId, String name, Language language, String bookType, String barcode, String issn, String isbn) {
            return this.bookDao.findBooks(publisher,authorId,name,language,barcode,issn,isbn,bookType)
                    .stream()
                    .map(BookEntity::toBook);
    }

}