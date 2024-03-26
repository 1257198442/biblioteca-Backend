package com.example.demo.adapters.mongodb.persistence;

import com.example.demo.adapters.mongodb.daos.AvatarRepository;
import com.example.demo.adapters.mongodb.daos.UserRepository;
import com.example.demo.adapters.mongodb.daos.WalletRepository;
import com.example.demo.adapters.mongodb.daos.CollectionListRepository;
import com.example.demo.adapters.mongodb.entities.AvatarEntity;
import com.example.demo.adapters.mongodb.entities.UserEntity;
import com.example.demo.adapters.mongodb.entities.WalletEntity;
import com.example.demo.adapters.mongodb.entities.CollectionListEntity;
import com.example.demo.domain.exceptions.NotFoundException;
import com.example.demo.domain.models.User;
import com.example.demo.domain.persistence.UserPersistence;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Repository
public class UserPersistenceMongodb implements UserPersistence {
    private final UserRepository userDao;
    private final AvatarRepository avatarDao;
    private final WalletRepository walletDao;
    private final CollectionListRepository wishListDao;

    @Autowired
    public UserPersistenceMongodb(UserRepository userDao,
                                  AvatarRepository avatarDao,
                                  WalletRepository walletDao,
                                  CollectionListRepository wishListDao){
        this.userDao = userDao;
        this.avatarDao = avatarDao;
        this.walletDao = walletDao;
        this.wishListDao = wishListDao;
    }

    @Override
    public User read(String telephone) {
        return this.userDao
                .readByTelephone(telephone)
                .orElseThrow(()->new NotFoundException("User telephone: "+telephone+" is not Fount"))
                .toUser();
    }

    @Override
    public Boolean existTelephone(String telephone) {
        return this.userDao.readByTelephone(telephone).isPresent();
    }

    @Override
    public User create(User user) {
        createNeedToDo(user);
        return this.userDao
                .save(new UserEntity(user))
                .toUser();
    }

    private void createNeedToDo(User user){
        this.avatarDao.save(new AvatarEntity("user.png","https://localhost/images/avatar/user.png",user.getTelephone(), LocalDateTime.now()));
        this.wishListDao.save(CollectionListEntity.builder().telephone(user.getTelephone()).bookId(List.of()).build());
        this.walletDao.save(new WalletEntity(new BigDecimal("0"),user.getTelephone()));
    }

    @Override
    public User update(User user) {
        UserEntity userEntity = this.userDao.readByTelephone(user.getTelephone())
                .orElseThrow(()->new NotFoundException("User telephone: "+user.getTelephone()+" is not Fount"));
        BeanUtils.copyProperties(user,userEntity);
        return this.userDao
                .save(userEntity)
                .toUser();
    }

    @Override
    public List<User> readAll() {
        return this.userDao.findAll().stream().map(UserEntity::toUser).collect(Collectors.toList());
    }

}