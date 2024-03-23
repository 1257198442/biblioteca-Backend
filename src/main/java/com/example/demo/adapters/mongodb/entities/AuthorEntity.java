package com.example.demo.adapters.mongodb.entities;

import com.example.demo.domain.models.Author;
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
public class AuthorEntity {
    @Id
    private String authorId;
    private String name;
    private String description;
    private String nationality;
    private String imgUrl;
    private String imgFileName;
    public AuthorEntity(Author author){
        BeanUtils.copyProperties(author,this);
    }

    public Author toAuthor(){
        Author author = new Author();
        BeanUtils.copyProperties(this,author);
        return author;
    }
}
