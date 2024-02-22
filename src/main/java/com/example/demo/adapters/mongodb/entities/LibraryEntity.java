package com.example.demo.adapters.mongodb.entities;

import com.example.demo.domain.models.Library;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LibraryEntity {
    private String address;
    @Id
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
    public Library toLibrary(){
        Library library = new Library();
        BeanUtils.copyProperties(this,library);
        return library;
    }
}