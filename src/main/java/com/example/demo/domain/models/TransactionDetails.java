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
public class TransactionDetails {
    public String lastName;
    public String firstName;
    public String city;
    public String billingAddress;
    public String postalCode;
}
