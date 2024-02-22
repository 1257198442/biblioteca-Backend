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
    private final LibraryRepository libraryDao;

    @Autowired
    public SeederDev(UserRepository userDao,LibraryRepository libraryDao){
        this.userDao = userDao;
        this.libraryDao = libraryDao;
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

        LogManager.getLogger(this.getClass()).warn("-------      Initial Library      -----------");
        LibraryEntity[] libraryEntities = {
                LibraryEntity.builder()
                        .address("X.º XX XXXXXXX, XX, 28001, XXXXXX, Madrid")
                        .telephone("6669996669")
                        .name("BIBLIOTECA")
                        .email("bibliotecaMadrid@email.com")
                        .postalCode("28001")
                        .businessHours("Monday to Friday 10:00 to 22:00 Saturday and Sunday 10:00 to 16:00")
                        .introduction("this is test introduction")
                        .twitter("https://www.twitter.com/")
                        .googleMail("https://mail.google.com/")
                        .instagram("https://www.instagram.com/")
                        .facebook("https://www.facebook.com/")
                        .discord("https://www.discord.com/").build()
        };
        this.libraryDao.saveAll(Arrays.asList(libraryEntities));
    }
}
