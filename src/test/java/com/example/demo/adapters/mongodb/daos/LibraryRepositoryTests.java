package com.example.demo.adapters.mongodb.daos;

import com.example.demo.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfig
public class LibraryRepositoryTests {
    @Autowired
    private LibraryRepository libraryRepository;
    @Test
    void testReadByName(){
        assertTrue(libraryRepository.readByName("BIBLIOTECA").isPresent());
        assertTrue(libraryRepository.readByName("null").isEmpty());
    }
}
