package com.example.demo.domain.service;

import com.example.demo.domain.models.Book;
import com.example.demo.domain.models.CollectionList;
import com.example.demo.domain.persistence.BookPersistence;
import com.example.demo.domain.persistence.CollectionListPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CollectionListService {
    private final BookPersistence bookPersistence;
    private final CollectionListPersistence collectionListPersistence;

    @Autowired
    public CollectionListService(BookPersistence bookPersistence, CollectionListPersistence collectionListPersistence){
        this.bookPersistence = bookPersistence;
        this.collectionListPersistence = collectionListPersistence;
    }

    public CollectionList read(String telephone){
        return this.collectionListPersistence.read(telephone);
    }
    public List<Book> readBookData(String telephone){
        return this.collectionListPersistence.read(telephone).bookId.stream()
                .map(this.bookPersistence::read)
                .collect(Collectors.toList());
    }
    public CollectionList addBook(String telephone, String bookId){
        CollectionList collectionList = this.collectionListPersistence.read(telephone);
        String bookIdAdd = this.bookPersistence.read(bookId).getBookID();
        collectionList.bookId.add(bookIdAdd);
        collectionList.bookId = collectionList.bookId.stream().distinct().collect(Collectors.toList());
        return this.collectionListPersistence.update(collectionList);
    }

}
