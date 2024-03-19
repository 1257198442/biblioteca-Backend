package com.example.demo.domain.persistence;

import com.example.demo.domain.models.Type;

import java.util.List;

public interface TypePersistence {
    Type create(Type type);
    Type read(String name);
    List<Type> readAll();
    Boolean existName(String name);
    Type getType(String name);

}