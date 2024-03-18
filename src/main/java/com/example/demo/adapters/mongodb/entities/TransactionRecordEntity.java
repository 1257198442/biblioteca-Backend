package com.example.demo.adapters.mongodb.entities;

import com.example.demo.domain.models.TransactionDetails;
import com.example.demo.domain.models.TransactionRecord;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionRecordEntity {
    public String reference;
    public String purpose;
    public String telephone;
    public BigDecimal amount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestampTime;
    private TransactionDetails transactionDetails;
    public TransactionRecordEntity(TransactionRecord transactionRecord){
        BeanUtils.copyProperties(transactionRecord,this);
    }
    public TransactionRecord toTransactionRecord(){
        TransactionRecord transactionRecord = new TransactionRecord();
        BeanUtils.copyProperties(this,transactionRecord);
        return transactionRecord;
    }
}
