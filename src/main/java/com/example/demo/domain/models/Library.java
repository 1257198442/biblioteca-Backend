package com.example.demo.domain.models;

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
public class Library {
    private String address;
    private String name;
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