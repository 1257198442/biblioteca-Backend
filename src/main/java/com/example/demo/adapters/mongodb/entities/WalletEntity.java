package com.example.demo.adapters.mongodb.entities;

import com.example.demo.domain.models.Wallet;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WalletEntity {
    public BigDecimal balance;
    @Id
    public String telephone;
    public Wallet toWallet(){
        Wallet wallet = new Wallet();
        BeanUtils.copyProperties(this,wallet);
        return wallet;
    }
}
