package com.example.demo.adapters.mongodb.persistence;

import com.example.demo.adapters.mongodb.daos.BookRepository;
import com.example.demo.adapters.mongodb.daos.ReturnDataRepository;
import com.example.demo.adapters.mongodb.daos.UserRepository;
import com.example.demo.adapters.mongodb.entities.BookEntity;
import com.example.demo.adapters.mongodb.entities.ReturnDataEntity;
import com.example.demo.adapters.mongodb.entities.UserEntity;
import com.example.demo.domain.exceptions.BadRequestException;
import com.example.demo.domain.exceptions.NotFoundException;
import com.example.demo.domain.models.ReturnData;
import com.example.demo.domain.persistence.ReturnDataPersistence;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ReturnDataPersistenceMongodb implements ReturnDataPersistence {
    private final ReturnDataRepository returnDataDao;
    private final BookRepository bookDao;
    private final UserRepository userDao;

    @Autowired
    public ReturnDataPersistenceMongodb(ReturnDataRepository returnDao, BookRepository bookDao, UserRepository userDao){
        this.returnDataDao = returnDao;
        this.bookDao = bookDao;
        this.userDao = userDao;
    }

    @Override
    public ReturnData create(ReturnData returnData) {
        ReturnDataEntity restitutionEntity = new ReturnDataEntity();
        BeanUtils.copyProperties(returnData,restitutionEntity);
        restitutionEntity.setBook(getBook(returnData));
        restitutionEntity.setUser(getUser(returnData));
        return this.returnDataDao
                .save(restitutionEntity)
                .toRestitution();
    }

    @Override
    public ReturnData read(String reference) {
        return this.getReturnData(reference)
                .toRestitution();
    }

    @Override
    public List<ReturnData> readAll() {
        return this.returnDataDao
                .findAll().stream().map(ReturnDataEntity::toRestitution)
                .collect(Collectors.toList());
    }

    @Override
    public ReturnData update(ReturnData returnData) {
        ReturnDataEntity returnDataEntity = this.getReturnData(returnData.getReference());
        BeanUtils.copyProperties(returnData,returnDataEntity);
        returnDataEntity.setBook(getBook(returnData));
        returnDataEntity.setUser(getUser(returnData));
        return this.returnDataDao
                .save(returnDataEntity)
                .toRestitution();
    }

    private ReturnDataEntity getReturnData(String reference){
        return this.returnDataDao.readByReference(reference)
                .orElseThrow(()->new NotFoundException("Return Data Reference: "+reference+" is not Fount"));
    }

    private BookEntity getBook(ReturnData returnData){
        if(returnData.getBook()!=null){
            return this.bookDao.readByBookID(returnData.getBook().getBookID())
                    .orElseThrow(()->new NotFoundException("BookId: "+returnData.getBook().getBookID()+" is not Fount"));
        }else {
            throw new BadRequestException("no have book data");
        }
    }

    private UserEntity getUser(ReturnData returnData){
        if(returnData.getUser()!=null){
            return this.userDao
                    .readByTelephone(returnData.getUser().getTelephone())
                    .orElseThrow(()->new NotFoundException("Telephone: "+returnData.getUser().getTelephone()+" is not Fount"));
        }else {
            throw new BadRequestException("no have user data");
        }
    }

}
