package com.example.demo.domain.persistence;

import com.example.demo.domain.models.Library;

public interface LibraryPersistence {
    Library update(Library library);
    Library read(String name);

}
