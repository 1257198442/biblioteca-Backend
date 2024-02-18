package com.example.demo.domain.service;

import com.example.demo.adapters.rest.dto.UserUploadDto;
import com.example.demo.domain.exceptions.ConflictException;
import com.example.demo.domain.exceptions.ForbiddenException;
import com.example.demo.domain.models.Role;
import com.example.demo.domain.models.User;
import com.example.demo.domain.persistence.UserPersistence;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {
    private final UserPersistence userPersistence;
    private final JwtService jwtService;
    private final PhoneNumberValidator phoneNumberValidator;

    @Autowired
    public UserService(UserPersistence userPersistence,JwtService jwtService,PhoneNumberValidator phoneNumberValidator){
        this.userPersistence = userPersistence;
        this.jwtService = jwtService;
        this.phoneNumberValidator = phoneNumberValidator;
    }

    public String login(String telephone) {
        User user= this.userPersistence.read(telephone);
        if(user.getRole()== Role.BAN){
            throw new ForbiddenException("Your account has been banned.: "+telephone);
        }
        return jwtService.createToken(user.getTelephone(), user.getName(), user.getRole().name());
}
    public User read(String telephone){
        return this.userPersistence.read(telephone).toShow();
    }
    public void assertUserNotExist(String telephone){
        if(this.userPersistence.existTelephone(telephone)){
            throw new ConflictException("User is Exist: "+telephone);
        }
    }
    public User create(UserUploadDto userUpload){
        this.assertUserNotExist(this.phoneNumberValidator.validate(userUpload.getTelephone()));
        User user = new User();
        BeanUtils.copyProperties(userUpload,user);
        user.setRole(Role.CLIENT);
        user.setCreateTime(LocalDateTime.now());
        user.setPassword(new BCryptPasswordEncoder().encode(userUpload.getPassword()));
        user.setActive(true);
        return this.userPersistence.create(user).toShow();
    }

}
