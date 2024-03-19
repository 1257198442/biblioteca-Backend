package com.example.demo.domain.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    public Book toShowOmit(){
        return Book.builder().bookID(this.bookID).name(this.name).status(this.status).deposit(this.deposit).imgUrl(this.imgUrl).language(this.language).authorId(this.authorId).build();
    }
}
