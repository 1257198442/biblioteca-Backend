package com.example.demo.domain.service;

import com.example.demo.domain.exceptions.ConflictException;
import com.example.demo.domain.models.Type;
import com.example.demo.domain.persistence.TypePersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeService {
    private final TypePersistence typePersistence;
    @Autowired
    public TypeService(TypePersistence typePersistence){
        this.typePersistence = typePersistence;
    }

    public Type create(Type type){
        this.assertTypeNotExist(type.getName());
        return this.typePersistence.create(type);
    }

    public void assertTypeNotExist(String name){
        if(this.typePersistence.existName(name)){
            throw new ConflictException("Type is Exist: "+name);
        }
    }

    public Type read(String name){
        return this.typePersistence.read(name);
    }

    public List<Type> readAll(){
        return this.typePersistence.readAll();
    }
    public Type getType(String name){
        return this.typePersistence.getType(name);
    }
}