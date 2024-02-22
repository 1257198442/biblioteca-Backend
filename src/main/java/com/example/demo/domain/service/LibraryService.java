package com.example.demo.domain.service;
import com.example.demo.adapters.rest.dto.LibraryUpdateDto;
import com.example.demo.domain.exceptions.ConflictException;
import com.example.demo.domain.models.Library;
import com.example.demo.domain.persistence.LibraryPersistence;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LibraryService {
    private final LibraryPersistence libraryPersistence;
    @Autowired
    public LibraryService(LibraryPersistence libraryPersistence){
        this.libraryPersistence = libraryPersistence;
    }

    public Library update(LibraryUpdateDto libraryUpdate, String name){
        Library library = new Library();
        BeanUtils.copyProperties(libraryUpdate,library);
        library.setName(name);
        return this.libraryPersistence.update(library);
    }

    public Library read(String name){
        return this.libraryPersistence.read(name);
    }

}