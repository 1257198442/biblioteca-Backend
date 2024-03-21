package com.example.demo.adapters.mongodb.persistence;

import com.example.demo.adapters.mongodb.daos.BookRepository;
import com.example.demo.adapters.mongodb.daos.LendingRepository;
import com.example.demo.adapters.mongodb.daos.UserRepository;
import com.example.demo.adapters.mongodb.entities.BookEntity;
import com.example.demo.adapters.mongodb.entities.LendingEntity;
import com.example.demo.adapters.mongodb.entities.UserEntity;
import com.example.demo.domain.exceptions.NotFoundException;
import com.example.demo.domain.models.Lending;
import com.example.demo.domain.persistence.LendingPersistence;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LendingPersistenceMongodb implements LendingPersistence {
    private final LendingRepository lendingDao;
    private final BookRepository bookDao;
    private final UserRepository userDao;
    @Autowired
    public LendingPersistenceMongodb(LendingRepository lendingDao, BookRepository bookDao, UserRepository userDao){
        this.lendingDao = lendingDao;
        this.bookDao = bookDao;
        this.userDao = userDao;
    }
    @Override
    public Lending create(Lending lending) {
        LendingEntity lendingEntity = new LendingEntity();
        BeanUtils.copyProperties(lending,lendingEntity);
            BookEntity bookEntity = this.bookDao
                    .readByBookID(lending.getBook().getBookID())
                    .orElseThrow(()->new NotFoundException("BookId: "+lending.getBook().getBookID()+" is not Fount"));
            lendingEntity.setBook(bookEntity);
            UserEntity userEntity = this.userDao
                    .readByTelephone(lending.getUser().getTelephone())
                    .orElseThrow(()->new NotFoundException("Telephone: "+lending.getUser().getTelephone()+" is not Fount"));
            lendingEntity.setUser(userEntity);
        return this.lendingDao
                .save(lendingEntity)
                .toLending();
    }

    @Override
    public Lending read(String reference) {
        return this.lendingDao
                .readByReference(reference)
                .orElseThrow(()->new NotFoundException("Reference: "+reference+" is not Fount"))
                .toLending();
    }

    @Override
    public List<Lending> readByUserTelephone(String telephone) {
        return this.lendingDao.readByUser_Telephone(telephone)
                .stream()
                .map(LendingEntity::toLending)
                .collect(Collectors.toList());
    }

    @Override
    public List<Lending> readAll() {
        return this.lendingDao.findAll().stream()
                .map(LendingEntity::toLending)
                .collect(Collectors.toList());
    }

}
