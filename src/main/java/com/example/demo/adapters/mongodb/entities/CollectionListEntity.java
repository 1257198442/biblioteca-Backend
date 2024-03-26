package com.example.demo.adapters.mongodb.entities;

import com.example.demo.domain.models.CollectionList;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollectionListEntity {
    @Id
    public String telephone;
    public List<String> bookId;

    public CollectionListEntity(CollectionList collectionList){
        BeanUtils.copyProperties(collectionList,this);
    }
    public CollectionList toWishList(){
        CollectionList collectionList =new CollectionList();
        BeanUtils.copyProperties(this, collectionList);
        return collectionList;
    }
}
