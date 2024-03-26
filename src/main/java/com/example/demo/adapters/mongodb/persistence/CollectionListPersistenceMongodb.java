package com.example.demo.adapters.mongodb.persistence;

import com.example.demo.adapters.mongodb.daos.CollectionListRepository;
import com.example.demo.adapters.mongodb.entities.CollectionListEntity;
import com.example.demo.domain.exceptions.NotFoundException;
import com.example.demo.domain.models.CollectionList;
import com.example.demo.domain.persistence.CollectionListPersistence;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CollectionListPersistenceMongodb implements CollectionListPersistence {
    private final CollectionListRepository wishListDao;
    @Autowired
    public CollectionListPersistenceMongodb(CollectionListRepository wishListDao){
        this.wishListDao = wishListDao;
    }


    @Override
    public CollectionList read(String telephone) {
        return this.wishListDao
                .readByTelephone(telephone)
                .orElseThrow(()->new NotFoundException("WishList: "+telephone+" is not Fount"))
                .toWishList();
    }

    @Override
    public CollectionList update(CollectionList collectionList) {
        CollectionListEntity wishListUpdate = this.wishListDao.readByTelephone(collectionList.telephone)
                 .orElseThrow(()->new NotFoundException("WishList: "+ collectionList.telephone+" is not Fount"));
         BeanUtils.copyProperties(collectionList,wishListUpdate);

        return this.wishListDao.save(wishListUpdate)
                 .toWishList();
    }
}
