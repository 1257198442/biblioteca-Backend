package com.example.demo.domain.models;

import com.example.demo.adapters.mongodb.entities.TypeEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Book {
    private String bookID;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime entryTime;
    private String description;
    private BookStatus status;
    private String publisher;
    private String imgUrl;
    private BigDecimal deposit;
    private Language language;
    private String isbn;
    private String issn;
    private String barcode;
    private List<String> authorId;
    private List<String> bookType;
    private String imgFileName;

    public Book toBook(){
        Book book = new Book();
        BeanUtils.copyProperties(this,book);
        return book;
    }

    public Book toShowOmit(){
        return Book.builder().bookID(this.bookID).name(this.name).status(this.status).deposit(this.deposit).imgUrl(this.imgUrl).language(this.language).authorId(this.authorId).bookType(this.bookType).description(this.description).build();
    }
}
