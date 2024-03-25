package com.example.demo.adapters.mongodb.persistence;

import com.example.demo.adapters.mongodb.daos.BookRepository;
import com.example.demo.adapters.mongodb.daos.LendingDataRepository;
import com.example.demo.adapters.mongodb.daos.UserRepository;
import com.example.demo.adapters.mongodb.entities.BookEntity;
import com.example.demo.adapters.mongodb.entities.LendingDataEntity;
import com.example.demo.adapters.mongodb.entities.UserEntity;
import com.example.demo.domain.exceptions.NotFoundException;
import com.example.demo.domain.models.LendingData;
import com.example.demo.domain.persistence.LendingDataPersistence;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LendingDataPersistenceMongodb implements LendingDataPersistence {
    private final LendingDataRepository lendingDataDao;
    private final BookRepository bookDao;
    private final UserRepository userDao;

    @Autowired
    public LendingDataPersistenceMongodb(LendingDataRepository lendingDataDao, BookRepository bookDao, UserRepository userDao){
        this.lendingDataDao = lendingDataDao;
        this.bookDao = bookDao;
        this.userDao = userDao;
    }

    @Override
    public LendingData create(LendingData lending) {
        LendingDataEntity lendingEntity = new LendingDataEntity();
        BeanUtils.copyProperties(lending,lendingEntity);
        lendingEntity.setBook(getBook(lending));
        lendingEntity.setUser(getUser(lending));
        return this.lendingDataDao
                .save(lendingEntity)
                .toLending();
    }

    @Override
    public LendingData read(String reference) {
        return this.lendingDataDao
                .readByReference(reference)
                .orElseThrow(()->new NotFoundException("Reference: "+reference+" is not Fount"))
                .toLending();
    }

    @Override
    public List<LendingData> readByUserTelephone(String telephone) {
        return this.lendingDataDao.readByUser_Telephone(telephone)
                .stream()
                .map(LendingDataEntity::toLending)
                .collect(Collectors.toList());
    }

    @Override
    public List<LendingData> readAll() {
        return this.lendingDataDao.findAll().stream()
                .map(LendingDataEntity::toLending)
                .collect(Collectors.toList());
    }

    @Override
    public LendingData update(LendingData lending) {
        LendingDataEntity lendingEntity =  this.lendingDataDao
                .readByReference(lending.getReference())
                .orElseThrow(()->new NotFoundException("Reference: "+lending.getReference()+" is not Fount"));
        BeanUtils.copyProperties(lending,lendingEntity);
        lendingEntity.setBook(this.getBook(lending));
        lendingEntity.setUser(this.getUser(lending));
        return this.lendingDataDao
                .save(lendingEntity)
                .toLending();
    }

    @Override
    public List<LendingData> readAllByNoReturn() {
        return  this.lendingDataDao.findAll().stream()
                .filter(lendingEntity -> lendingEntity.getStatus().equals(false))
                .map(LendingDataEntity::toLending)
                .collect(Collectors.toList());
    }

    private BookEntity getBook(LendingData lendingData){
        return this.bookDao
                .readByBookID(lendingData.getBook().getBookID())
                .orElseThrow(()->new NotFoundException("BookId: "+lendingData.getBook().getBookID()+" is not Fount"));
    }

    private UserEntity getUser(LendingData lendingData){
        return this.userDao
                .readByTelephone(lendingData.getUser().getTelephone())
                .orElseThrow(()->new NotFoundException("Telephone: "+lendingData.getUser().getTelephone()+" is not Fount"));
    }

}
