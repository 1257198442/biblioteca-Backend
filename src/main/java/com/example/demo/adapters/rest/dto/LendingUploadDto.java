package com.example.demo.adapters.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LendingUploadDto {
    private String bookId;
    private String telephone;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String limitTime;
}
