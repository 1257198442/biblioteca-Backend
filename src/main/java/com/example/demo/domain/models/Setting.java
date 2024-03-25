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
public class Setting {
    private Boolean hideMyProfile;
    private Boolean emailWhenSuccessfulTransaction;
    private Boolean emailWhenOrderIsPaid;
    private Boolean emailWhenOrdersAboutToExpire;

    public Setting init(){
        return Setting.builder()
                .hideMyProfile(true)
                .emailWhenSuccessfulTransaction(true)
                .emailWhenOrderIsPaid(true)
                .emailWhenOrdersAboutToExpire(true).build();
    }
}