package com.example.demo.adapters.mongodb.persistence;

import com.example.demo.adapters.mongodb.daos.LibraryRepository;
import com.example.demo.adapters.mongodb.entities.LibraryEntity;
import com.example.demo.domain.exceptions.NotFoundException;
import com.example.demo.domain.models.Library;
import com.example.demo.domain.persistence.LibraryPersistence;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LibraryPersistenceMongodb implements LibraryPersistence {
    private final LibraryRepository libraryRepository;

    @Autowired
    public LibraryPersistenceMongodb(LibraryRepository libraryRepository){
        this.libraryRepository = libraryRepository;
    }

    @Override
    public Library read(String name) {
        return this.libraryRepository.readByName(name)
                .orElseThrow(()->new NotFoundException("Library Name: "+name+" is not Fount"))
                .toLibrary();
    }

    @Override
    public Library update(Library library) {
        LibraryEntity libraryEntity = this.libraryRepository
                .readByName(library.getName())
                .orElseThrow(()->new NotFoundException("Library Name: "+library.getName()+" is not Fount"));
        BeanUtils.copyProperties(library,libraryEntity);
        return this.libraryRepository
                .save(libraryEntity)
                .toLibrary();
    }

}