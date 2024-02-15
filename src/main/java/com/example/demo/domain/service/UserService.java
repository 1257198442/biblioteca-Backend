package com.example.demo.domain.service;

import com.example.demo.domain.exceptions.ForbiddenException;
import com.example.demo.domain.models.Admin;
import com.example.demo.domain.models.User;
import com.example.demo.domain.persistence.UserPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserPersistence userPersistence;
    private final JwtService jwtService;

    @Autowired
    public UserService(UserPersistence userPersistence,JwtService jwtService){
        this.userPersistence = userPersistence;
        this.jwtService = jwtService;
    }

    public String login(String telephone) {
        User user= this.userPersistence.read(telephone);
        if(user.getAdmin()==Admin.BAN){
            throw new ForbiddenException("Your account has been banned.: "+telephone);
        }
        return jwtService.createToken(user.getTelephone(), user.getName(), user.getAdmin().name());
}

    public User read(String telephone){
        return this.userPersistence.read(telephone).toShow();
    }


}
