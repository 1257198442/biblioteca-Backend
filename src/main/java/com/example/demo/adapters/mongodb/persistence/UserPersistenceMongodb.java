package com.example.demo.adapters.mongodb.persistence;

import com.example.demo.adapters.mongodb.daos.UserRepository;
import com.example.demo.domain.exceptions.NotFoundException;
import com.example.demo.domain.models.User;
import com.example.demo.domain.persistence.UserPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


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

}
