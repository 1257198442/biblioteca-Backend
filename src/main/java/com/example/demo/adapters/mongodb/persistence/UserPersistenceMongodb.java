package com.example.demo.adapters.mongodb.persistence;

import com.example.demo.adapters.mongodb.daos.UserRepository;
import com.example.demo.adapters.mongodb.entities.UserEntity;
import com.example.demo.domain.exceptions.NotFoundException;
import com.example.demo.domain.models.User;
import com.example.demo.domain.persistence.UserPersistence;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;


@Repository
public class UserPersistenceMongodb implements UserPersistence {
    private final UserRepository userRepository;

    @Autowired
    public UserPersistenceMongodb(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public User read(String telephone) {
        return this.userRepository
                .readByTelephone(telephone)
                .orElseThrow(()->new NotFoundException("User telephone: "+telephone+" is not Fount"))
                .toUser();
    }

    @Override
    public Boolean existTelephone(String telephone) {
        return this.userRepository.readByTelephone(telephone).isPresent();
    }

    @Override
    public User create(User user) {
        return this.userRepository
                .save(new UserEntity(user))
                .toUser();
    }

    @Override
    public User update(User user) {
        UserEntity userEntity = this.userRepository.readByTelephone(user.getTelephone())
                .orElseThrow(()->new NotFoundException("User telephone: "+user.getTelephone()+" is not Fount"));
        BeanUtils.copyProperties(user,userEntity);
        return this.userRepository
                .save(userEntity)
                .toUser();
    }

    @Override
    public List<User> readAll() {
        return this.userRepository.findAll().stream().map(UserEntity::toUser).collect(Collectors.toList());
    }

}
