package com.example.demo.adapters.rest.dto;

import com.example.demo.domain.models.Setting;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserUpdateDto {
    private String name;
    private String description;
    private String email;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdays;
    private Setting setting;
}
