package com.example.demo.adapters.mongodb.entities;

import com.example.demo.domain.models.Role;
import com.example.demo.domain.models.Setting;
import com.example.demo.domain.models.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserEntity {
    private String name;
    private String password;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @Id
    private String telephone;
    private String email;
    private Role role;
    private Boolean active;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdays;
    private Setting setting;
    public UserEntity(User user){
        BeanUtils.copyProperties(user,this);
    }

    public User toUser(){
        User user = new User();
        BeanUtils.copyProperties(this,user);
        return user;
    }
}
