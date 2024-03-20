package com.example.demo.adapters.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookUpdateDto {
    private String name;
    private List<String> bookType;
    private String description;
    private List<String> authorId;
    private String publisher;
    private BigDecimal deposit;
    private String language;
    private String barcode;
    private String isbn;
    private String issn;
}
