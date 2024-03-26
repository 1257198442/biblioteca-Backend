package com.example.demo.domain.service;

import com.example.demo.TestConfig;
import com.example.demo.domain.models.CollectionList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestConfig
public class CollectionListServiceTests {
    @Autowired
    private CollectionListService collectionListService;
    @Test
    void testReadAndAddBookAndReadBookDataAndRemoveBook(){
        CollectionList collectionList = collectionListService.read("+34990099009");
        assertEquals(List.of(),collectionList.bookId);
        collectionListService.addBook("+34990099009","1");
        assertEquals(List.of("1"),collectionListService.read("+34990099009").getBookId());
        collectionListService.addBook("+34990099009","2");
        assertEquals(2,collectionListService.readBookData("+34990099009").size());
        collectionListService.removeBook("+34990099009","2");
        assertEquals(List.of("1"),collectionListService.read("+34990099009").getBookId());


    }
}
