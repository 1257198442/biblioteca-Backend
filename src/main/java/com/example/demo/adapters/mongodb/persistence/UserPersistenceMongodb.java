package com.example.demo.adapters.mongodb.persistence;

import com.example.demo.adapters.mongodb.daos.AvatarRepository;
import com.example.demo.adapters.mongodb.daos.UserRepository;
import com.example.demo.adapters.mongodb.entities.AvatarEntity;
import com.example.demo.adapters.mongodb.entities.UserEntity;
import com.example.demo.domain.exceptions.NotFoundException;
import com.example.demo.domain.models.User;
import com.example.demo.domain.persistence.UserPersistence;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Repository
public class UserPersistenceMongodb implements UserPersistence {
    private final UserRepository userRepository;
    private final AvatarRepository avatarRepository;

    @Autowired
    public UserPersistenceMongodb(UserRepository userRepository,
                                  AvatarRepository avatarRepository){
        this.userRepository = userRepository;
        this.avatarRepository = avatarRepository;
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
        createNeedToBe(user);
        return this.userRepository
                .save(new UserEntity(user))
                .toUser();
    }

    private void createNeedToBe(User user){
        this.avatarRepository.save(new AvatarEntity("user.png","https://localhost/images/avatar/user.png",user.getTelephone(), LocalDateTime.now()));
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