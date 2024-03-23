package com.example.demo.adapters.mongodb.entities;

import com.example.demo.domain.models.ReturnData;
import com.example.demo.domain.models.ReturnStatus;
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
public class ReturnDataEntity {
    @Id
    private String reference;
    private BookEntity book;
    private UserEntity user;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime restitutionTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lendingTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime limitTime;
    private ReturnStatus returnStatus;


    public ReturnData toRestitution(){
        ReturnData restitution = new ReturnData();
        BeanUtils.copyProperties(this,restitution);
        if(this.book!=null){
            restitution.setBook(this.book.toBook());
        }
        if(this.book!=null){
            restitution.setUser(this.user.toUser());
        }
        return restitution;
    }
}
