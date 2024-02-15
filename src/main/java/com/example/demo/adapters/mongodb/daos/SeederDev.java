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
                UserEntity.builder().admin(Admin.ROOT).createTime(LocalDateTime.now()).email("1257198442@qq.com").name("root").password(pass).telephone("666666666").active(true).build(),
                UserEntity.builder().admin(Admin.ADMINISTRATOR).createTime(LocalDateTime.now()).email("1257198442@qq.com").name("Administrator").password(pass).telephone("666000001").active(true).build(),
                UserEntity.builder().admin(Admin.CLIENT).createTime(LocalDateTime.now()).email("1257198442@qq.com").name("User").password(pass).telephone("666000002").active(true).build(),
                UserEntity.builder().admin(Admin.BAN).createTime(LocalDateTime.now()).email("1257198442@qq.com").name("BAN").password(pass).telephone("666").active(true).build(),
        };
        userDao.saveAll(Arrays.asList(userEntities));
    }
}
