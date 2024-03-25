package com.example.demo.adapters.mongodb.entities;

import com.example.demo.domain.models.LendingData;
import com.example.demo.domain.models.ReturnSendEmail;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LendingDataEntity {
    @Id
    private String reference;
    private BookEntity book;
    private UserEntity user;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lendingTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime limitTime;
    private Boolean status;
    private ReturnSendEmail returnSendEmail;
    public LendingData toLending(){
        LendingData lending = new LendingData();
        BeanUtils.copyProperties(this,lending);
        if(this.book!=null){
            lending.setBook(this.book.toBook());
        }
        if(this.user!=null){
            lending.setUser(this.user.toUser());
        }
        return lending;
    }
}
