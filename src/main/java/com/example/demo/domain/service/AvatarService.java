package com.example.demo.domain.service;

import com.example.demo.domain.models.Avatar;
import com.example.demo.domain.persistence.AvatarPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvatarService {
    private final AvatarPersistence avatarPersistence;
    @Autowired
    public AvatarService(AvatarPersistence avatarPersistence){
        this.avatarPersistence = avatarPersistence;
    }

    public Avatar update(Avatar avatar){
        return this.avatarPersistence.update(avatar);
    }
    public Avatar read(String telephone){
        return this.avatarPersistence.read(telephone);
    }
}
