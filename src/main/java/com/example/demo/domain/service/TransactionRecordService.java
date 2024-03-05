package com.example.demo.domain.service;

import com.example.demo.adapters.rest.dto.TransactionRecordDto;
import com.example.demo.domain.models.TransactionRecord;
import com.example.demo.domain.persistence.TransactionRecordPersistence;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionRecordService {
    private final TransactionRecordPersistence transactionRecordPersistence;
    private final RandomStringService randomStringService;

    @Autowired
    public TransactionRecordService(TransactionRecordPersistence transactionRecordPersistence,
                                    RandomStringService randomStringService){
        this.transactionRecordPersistence = transactionRecordPersistence;
        this.randomStringService = randomStringService;
    }

    public TransactionRecord create(TransactionRecordDto transactionRecordData){
        TransactionRecord transactionRecord = new TransactionRecord();
        BeanUtils.copyProperties(transactionRecordData,transactionRecord);
        transactionRecord.setReference(randomStringService.generateRandomString(12));
        transactionRecord.setTimestampTime(LocalDateTime.now());
        //TODO Determine if user settings require email alerts
        return this.transactionRecordPersistence.create(transactionRecord);
    }

    public void sendEmail(){
        //TODO send email
    }
}