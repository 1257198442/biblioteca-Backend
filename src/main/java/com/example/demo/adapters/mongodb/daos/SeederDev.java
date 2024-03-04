package com.example.demo.adapters.mongodb.daos;

import com.example.demo.adapters.mongodb.entities.*;
import com.example.demo.domain.models.*;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@Service
public class SeederDev {
    private final UserRepository userDao;
    private final LibraryRepository libraryDao;
    private final AvatarRepository avatarDao;
    private final WalletRepository walletDao;

    @Autowired
    public SeederDev(UserRepository userDao,
                     LibraryRepository libraryDao,
                     AvatarRepository avatarDao,
                     WalletRepository walletDao){
        this.userDao = userDao;
        this.libraryDao = libraryDao;
        this.avatarDao = avatarDao;
        this.walletDao = walletDao;
        this.deleteAllAndInitializeAndSeedDataBase();
    }
    public void deleteAllAndInitializeAndSeedDataBase() {
        this.deleteAllAndInitialize();
        this.seedDataBase();
    }

    public void deleteAllAndInitialize() {
        this.userDao.deleteAll();
        this.libraryDao.deleteAll();
        this.avatarDao.deleteAll();
    }
    private void seedDataBase() {
        LogManager.getLogger(this.getClass()).warn("------- Initial Load from JAVA -----------");
        LogManager.getLogger(this.getClass()).warn("-------      Initial User      -----------");
        String pass = new BCryptPasswordEncoder().encode("6");
        Setting setting = Setting.builder().hideMyProfile(true).build();
        UserEntity[] userEntities = {
                UserEntity.builder().role(Role.ROOT).createTime(LocalDateTime.now()).email("1257198442@qq.com").name("root").password(pass).telephone("+34666666666").active(true).description("I am is root").birthdays(LocalDate.of(2000,1,1)).setting(setting).build(),
                UserEntity.builder().role(Role.ADMINISTRATOR).createTime(LocalDateTime.now()).email("1257198442@qq.com").name("Administrator").password(pass).telephone("+34666000001").active(true).description("I am is administrator").birthdays(LocalDate.of(2000,1,2)).setting(setting).build(),
                UserEntity.builder().role(Role.CLIENT).createTime(LocalDateTime.now()).email("1257198442@qq.com").name("User").password(pass).telephone("+34666000002").active(true).description("I am is client").birthdays(LocalDate.of(2000,1,3)).setting(setting).build(),
                UserEntity.builder().role(Role.BAN).createTime(LocalDateTime.now()).email("test1@test.com").name("BAN").password(pass).telephone("+34666").active(true).description("An root").birthdays(LocalDate.of(2000,1,4)).setting(setting).build(),
                UserEntity.builder().role(Role.CLIENT).createTime(LocalDateTime.now()).email("test2@test.com").name("User").password(pass).telephone("+34645321068").active(true).description("An root").birthdays(LocalDate.of(2000,1,5)).setting(setting).build(),
                UserEntity.builder().role(Role.CLIENT).createTime(LocalDateTime.now()).email("test2@test.com").name("User").password(pass).telephone("+34123").active(true).description("An root").birthdays(LocalDate.of(2000,1,6)).setting(setting).build(),
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

        LogManager.getLogger(this.getClass()).warn("-------      Initial Library      -----------");
        AvatarEntity[] avatarEntities = {
                AvatarEntity.builder().fileName("user.png").url("https://localhost/images/avatar/user.png").telephone("+34666666666").uploadTime(LocalDateTime.now()).build(),
                AvatarEntity.builder().fileName("user.png").url("https://localhost/images/avatar/user.png").telephone("+34666000001").uploadTime(LocalDateTime.now()).build(),
                AvatarEntity.builder().fileName("user.png").url("https://localhost/images/avatar/user.png").telephone("+34666000002").uploadTime(LocalDateTime.now()).build(),
                AvatarEntity.builder().fileName("user.png").url("https://localhost/images/avatar/user.png").telephone("+34666").uploadTime(LocalDateTime.of(2000,1,1,1,1,1)).build(),
                AvatarEntity.builder().fileName("user.png").url("https://localhost/images/avatar/user.png").telephone("+34645321068").uploadTime(LocalDateTime.now()).build(),
                AvatarEntity.builder().fileName("user.png").url("https://localhost/images/avatar/user.png").telephone("+34123").uploadTime(LocalDateTime.now()).build(),
        };
        this.avatarDao.saveAll(Arrays.asList(avatarEntities));

        LogManager.getLogger(this.getClass()).warn("-------      Initial Wallet      -----------");
        WalletEntity[] walletEntities = {
                WalletEntity.builder().telephone("+34666666666").balance(new BigDecimal("999")).build(),
                WalletEntity.builder().telephone("+34666000001").balance(new BigDecimal("0")).build(),
                WalletEntity.builder().telephone("+34666000002").balance(new BigDecimal("0")).build(),
                WalletEntity.builder().telephone("+34666").balance(new BigDecimal("0")).build(),
                WalletEntity.builder().telephone("+34645321068").balance(new BigDecimal("0")).build(),
                WalletEntity.builder().telephone("+34123").balance(new BigDecimal("235")).build()
        };
        this.walletDao.saveAll(Arrays.asList(walletEntities));
    }

}
