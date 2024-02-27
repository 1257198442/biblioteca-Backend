package com.example.demo.adapters.rest.dto;

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
public class LibraryUpdateDto {
    private String address;
    private String telephone;
    private String email;
    private String postalCode;
    private String businessHours;
    private String introduction;
    private String googleMail;
    private String instagram;
    private String facebook;
    private String discord;
    private String twitter;
}