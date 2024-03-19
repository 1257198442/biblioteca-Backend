package com.example.demo.adapters.rest.show;

import com.example.demo.domain.models.Author;
import com.example.demo.domain.models.BookStatus;
import com.example.demo.domain.models.Language;
import com.example.demo.domain.models.Type;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookByShow {
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
    private int borrowCount;

    private String isbn;
    private String issn;
    private String barcode;
    private List<Author> author;
    private List<Type> types;
}
