package com.example.demo.domain.service;

import com.example.demo.adapters.rest.dto.UserUpdateDto;
import com.example.demo.adapters.rest.dto.UserUploadDto;
import com.example.demo.domain.exceptions.ConflictException;
import com.example.demo.domain.exceptions.ForbiddenException;
import com.example.demo.domain.models.Role;
import com.example.demo.domain.models.Setting;
import com.example.demo.domain.models.User;
import com.example.demo.domain.persistence.UserPersistence;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        return this.userPersistence.create(initUser(userUpload)).toShow();
    }

    public User initUser(UserUploadDto userUpload){
        Setting setting = new Setting().init();
        return User.builder()
                .name(userUpload.getName())
                .password(new BCryptPasswordEncoder().encode(userUpload.getPassword()))
                .telephone(userUpload.getTelephone())
                .email(userUpload.getEmail())
                .role(Role.CLIENT)
                .createTime(LocalDateTime.now())
                .active(true)
                .description("This user has not modified his profile")
                .birthdays(LocalDate.of(1990,1,1))
                .setting(setting).build();
    }

    public User updateRoleROOT(String telephone, String role){
        User user = this.userPersistence.read(telephone);
        if(user.getRole().equals(Role.ROOT)||role.equals("ROOT")){
            throw new ForbiddenException("Root cannot be changed.");
        }
        //TODO
        //check if user has a record of borrowed books that have not been returned.
//        if(){
//            throw new ForbiddenException("User "+telephone+" has a record of borrowed books that have not been returned.");
//        }
        user.setActive(!role.equals("BAN"));
        user.setRole(Role.fromString(role));
        return this.userPersistence.update(user).toShow();
    }

    public User updateRoleADMINISTRATOR(String telephone, String role){
        User user = this.userPersistence.read(telephone);
        if(user.getRole().equals(Role.ROOT)||role.equals("ROOT")){
            throw new ForbiddenException("Root cannot be changed.");
        }
        //TODO
        //check if user has a record of borrowed books that have not been returned.
//        if(){
//            throw new ForbiddenException("User "+telephone+" has a record of borrowed books that have not been returned.");
//        }
        if (!role.equals("ADMINISTRATOR")) {
            throw new ForbiddenException("You don't have permission to make this request.");
        }
        user.setRole(Role.fromString(role));
        return this.userPersistence.update(user).toShow();
    }

    public List<User> readAll(){
        return this.userPersistence.readAll().stream().map(User::toShow).collect(Collectors.toList());
    }

    public User update(String telephone, UserUpdateDto userUpdate){
        User user = this.userPersistence.read(telephone);
        BeanUtils.copyProperties(userUpdate,user);
        return this.userPersistence.update(user).toShow();
    }
    public User updateSetting(String telephone,Setting setting){
        User user = this.userPersistence.read(telephone);
        Setting setting1 = new Setting();
        BeanUtils.copyProperties(setting,setting1);
        user.setSetting(setting);
        return this.userPersistence.update(user).toShow();
    }

    public User changePassword(String telephone,String password){
        User user = this.userPersistence.read(telephone);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        return this.userPersistence.update(user).toShow();
    }

    public String getUserPassword(String telephone){
        return this.userPersistence.read(telephone).getPassword();
    }
}