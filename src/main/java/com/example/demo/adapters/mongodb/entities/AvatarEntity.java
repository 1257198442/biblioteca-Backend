package com.example.demo.adapters.mongodb.entities;

import com.example.demo.domain.models.Avatar;
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
public class AvatarEntity {
    public String fileName;
    public String url;
    @Id
    public String telephone;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime uploadTime;
    public AvatarEntity(Avatar avatar){
        BeanUtils.copyProperties(avatar,this);
    }
    public Avatar toAvatar(){
        Avatar avatar = new Avatar();
        BeanUtils.copyProperties(this,avatar);
        return avatar;
    }
}
