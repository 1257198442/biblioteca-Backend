package com.example.demo.adapters.mongodb.persistence;

import com.example.demo.adapters.mongodb.daos.AvatarRepository;
import com.example.demo.adapters.mongodb.entities.AvatarEntity;
import com.example.demo.domain.exceptions.NotFoundException;
import com.example.demo.domain.models.Avatar;
import com.example.demo.domain.persistence.AvatarPersistence;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AvatarPersistenceMongodb implements AvatarPersistence {
    private final AvatarRepository avatarDao;
    @Autowired
    public AvatarPersistenceMongodb(AvatarRepository avatarDao){
        this.avatarDao = avatarDao;
    }
    @Override
    public Avatar create(Avatar avatar) {
        return this.avatarDao.save(new AvatarEntity(avatar)).toAvatar();
    }

    @Override
    public Avatar read(String telephone) {
        return this.avatarDao.readByTelephone(telephone)
                .orElseThrow(()->new NotFoundException("Avatar telephone: "+telephone+" is not Fount"))
                .toAvatar();
    }

    @Override
    public Avatar update(Avatar avatar) {
        AvatarEntity avatarEntity = this.avatarDao.readByTelephone(avatar.getTelephone())
                .orElseThrow(()->new NotFoundException("Avatar telephone: "+avatar.getTelephone()+" is not Fount"));
        BeanUtils.copyProperties(avatar,avatarEntity);
        return this.avatarDao.save(avatarEntity).toAvatar();
    }


}
