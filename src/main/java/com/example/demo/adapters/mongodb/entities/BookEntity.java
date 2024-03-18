package com.example.demo.adapters.mongodb.entities;

import com.example.demo.domain.models.Book;
import com.example.demo.domain.models.BookStatus;
import com.example.demo.domain.models.Language;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookEntity {
    @Id
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

    public Book toBook(){
        Book book = new Book();
        BeanUtils.copyProperties(this,book);
        return book;
    }

}
