package com.example.demo.domain.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    private String name;
    private String password;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    private String telephone;
    private String email;
    private Role role;
    private Boolean active;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdays;
    private Setting setting;
    public User toShow(){
        User user = new User();
        BeanUtils.copyProperties(this,user);
        user.setPassword(null);
        user.setActive(null);
        return user;
    }
}
