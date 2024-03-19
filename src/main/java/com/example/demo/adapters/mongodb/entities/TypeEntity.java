package com.example.demo.adapters.mongodb.entities;

import com.example.demo.domain.models.Type;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TypeEntity {
    @Id
    private String name;
    private String description;
    public TypeEntity(Type type){
        BeanUtils.copyProperties(type,this);
    }
    public Type toType(){
        Type type = new Type();
        BeanUtils.copyProperties(this,type);
        return type;
    }
}
