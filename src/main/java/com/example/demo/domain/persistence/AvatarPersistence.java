package com.example.demo.domain.persistence;

import com.example.demo.domain.models.Avatar;

public interface AvatarPersistence {
    Avatar update(Avatar avatar);
    Avatar read(String telephone);
}