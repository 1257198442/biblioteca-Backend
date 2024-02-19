package com.example.demo.adapters.mongodb.daos;

import com.example.demo.adapters.mongodb.entities.*;
import com.example.demo.domain.models.*;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service
public class SeederDev {
    private final UserRepository userDao;

    @Autowired
    public SeederDev(UserRepository userDao){
        this.userDao = userDao;
        this.deleteAllAndInitializeAndSeedDataBase();
    }
    public void deleteAllAndInitializeAndSeedDataBase() {
        this.deleteAllAndInitialize();
        this.seedDataBase();
    }

    public void deleteAllAndInitialize() {
        this.userDao.deleteAll();
    }
    private void seedDataBase() {
        LogManager.getLogger(this.getClass()).warn("------- Initial Load from JAVA -----------");
        LogManager.getLogger(this.getClass()).warn("-------      Initial User      -----------");
        String pass = new BCryptPasswordEncoder().encode("6");
        UserEntity[] userEntities = {
                UserEntity.builder().role(Role.ROOT).createTime(LocalDateTime.now()).email("1257198442@qq.com").name("root").password(pass).telephone("+34666666666").active(true).build(),
                UserEntity.builder().role(Role.ADMINISTRATOR).createTime(LocalDateTime.now()).email("1257198442@qq.com").name("Administrator").password(pass).telephone("+34666000001").active(true).build(),
                UserEntity.builder().role(Role.CLIENT).createTime(LocalDateTime.now()).email("1257198442@qq.com").name("User").password(pass).telephone("+34666000002").active(true).build(),
                UserEntity.builder().role(Role.BAN).createTime(LocalDateTime.now()).email("test1@test.com").name("BAN").password(pass).telephone("+34666").active(true).build(),
                UserEntity.builder().role(Role.CLIENT).createTime(LocalDateTime.now()).email("test2@test.com").name("User").password(pass).telephone("+34645321068").active(true).build(),
                UserEntity.builder().role(Role.CLIENT).createTime(LocalDateTime.now()).email("test2@test.com").name("User").password(pass).telephone("+34123").active(true).build(),
        };
        userDao.saveAll(Arrays.asList(userEntities));
    }
}
