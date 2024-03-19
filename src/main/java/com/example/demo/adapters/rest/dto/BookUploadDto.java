package com.example.demo.adapters.rest.dto;

import com.example.demo.domain.models.Author;
import com.example.demo.domain.models.Type;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookUploadDto {
    private String name;
    private String description;
    private String publisher;
    private BigDecimal deposit;
    private String language;
    private String isbn;
    private String issn;
    private String barcode;
    private List<String> authorId;
    private List<String> BookType;
}
