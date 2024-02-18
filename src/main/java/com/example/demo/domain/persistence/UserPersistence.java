package com.example.demo.domain.persistence;

import com.example.demo.domain.models.User;

import java.util.List;

public interface UserPersistence {
    User read(String telephone);
    Boolean existTelephone(String telephone);
    User create(User user);

}