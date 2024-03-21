package com.example.demo;

import com.example.demo.adapters.mongodb.daos.AvatarRepository;
import com.example.demo.adapters.mongodb.daos.LibraryRepository;
import com.example.demo.adapters.mongodb.daos.UserRepository;
import com.example.demo.adapters.mongodb.daos.WalletRepository;
import com.example.demo.adapters.mongodb.entities.AvatarEntity;
import com.example.demo.adapters.mongodb.entities.LibraryEntity;
import com.example.demo.adapters.mongodb.entities.UserEntity;
import com.example.demo.adapters.mongodb.entities.WalletEntity;
import com.example.demo.domain.models.Role;
import com.example.demo.domain.models.Setting;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class DemoApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
    @Bean
    CommandLineRunner init(LibraryRepository libraryDao,
                           UserRepository userDao,
                           WalletRepository walletDao,
                           AvatarRepository avatarRepository) {
        return args -> {
            if(libraryDao.findAll().size()==0){
                LibraryEntity[] libraryEntities = {
                        LibraryEntity.builder()
                                .address("X.º XX XXXXXXX, XX, 28001, XXXXXX, Madrid")
                                .telephone("6669996669")
                                .name("BIBLIOTECA")
                                .email("bibliotecaMadrid@email.com")
                                .postalCode("28001")
                                .businessHours("Monday to Friday 10:00 to 22:00 Saturday and Sunday 10:00 to 16:00")
                                .introduction("this is test introduction")
                                .twitter("httpss://www.twitter.com/")
                                .googleMail("httpss://mail.google.com/")
                                .instagram("httpss://www.instagram.com/")
                                .facebook("httpss://www.facebook.com/")
                                .discord("httpss://www.discord.com/").build()
                };
                libraryDao.saveAll(Arrays.asList(libraryEntities));
            }
            if(userDao.findAll().size()==0){
                Setting setting = Setting.builder()
                        .emailWhenSuccessfulTransaction(true)
                        .hideMyProfile(false).build();
                UserEntity[] userEntities = {
                        UserEntity.builder().role(Role.ROOT).createTime(LocalDateTime.now()).email("1257198442@qq.com").name("root").password(new BCryptPasswordEncoder().encode("6")).telephone("+34666666666").description("An root").birthdays(LocalDate.of(2000,7,21)).active(true).setting(setting).build(),
                };
                userDao.saveAll(Arrays.asList(userEntities));
                WalletEntity[] walletEntities = {
                        WalletEntity.builder().balance(new BigDecimal("0")).telephone("+34666666666").build()
                };
                walletDao.saveAll(Arrays.asList(walletEntities));
                AvatarEntity[] avatarEntities = {
                        AvatarEntity.builder().fileName("user.png").url("https://localhost/images/avatar/user.png").telephone("+34666666666").uploadTime(LocalDateTime.now()).build()
                };
                avatarRepository.saveAll(Arrays.asList(avatarEntities));
            }
        };
    }
}