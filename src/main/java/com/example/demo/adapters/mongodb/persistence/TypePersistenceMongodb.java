package com.example.demo.adapters.mongodb.persistence;

import com.example.demo.adapters.mongodb.daos.TypeRepository;
import com.example.demo.adapters.mongodb.entities.AuthorEntity;
import com.example.demo.adapters.mongodb.entities.TypeEntity;
import com.example.demo.domain.exceptions.NotFoundException;
import com.example.demo.domain.models.Author;
import com.example.demo.domain.models.Type;
import com.example.demo.domain.persistence.TypePersistence;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TypePersistenceMongodb implements TypePersistence {
    private final TypeRepository typeDao;
    @Autowired
    public TypePersistenceMongodb(TypeRepository typeDao){
        this.typeDao = typeDao;
    }
    @Override
    public Type create(Type type) {
        return this.typeDao.save(new TypeEntity(type)).toType();
    }

    @Override
    public Boolean existName(String name) {
        return this.typeDao.readByName(name).isPresent();
    }

    @Override
    public Type read(String name) {
        return this.typeDao
                .readByName(name)
                .orElseThrow(()->new NotFoundException("Type: "+name+" is not Fount"))
                .toType();
    }

    @Override
    public List<Type> readAll(){
        return this.typeDao.findAll()
                .stream()
                .map(TypeEntity::toType)
                .collect(Collectors.toList());
    }

    @Override
    public Type getType(String name) {
        Optional<TypeEntity> type = this.typeDao.readByName(name);
        return type.map(TypeEntity::toType).orElse(null);
    }

    @Override
    public Type update(Type type) {
        TypeEntity typeEntity = this.typeDao
                .readByName(type.getName())
                .orElseThrow(()->new NotFoundException("Type: "+type.getName()+" is not Fount"));
        BeanUtils.copyProperties(type,typeEntity);

        return this.typeDao
                .save(typeEntity)
                .toType();
    }

    @Override
    public Type delete(String name) {

        return this.typeDao
                .deleteByName(name)
                .orElseThrow(()->new NotFoundException("Type: "+name+" is not Fount"))
                .toType();
    }

}