package com.example.demo.adapters.rest.dto;

import com.example.demo.domain.models.TransactionDetails;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionRecordDto {
    public String password;
    public String purpose;
    public String telephone;
    public BigDecimal amount;
    public TransactionDetails transactionDetails;
}