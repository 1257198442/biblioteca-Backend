package com.example.demo.domain.service;

import com.example.demo.adapters.rest.dto.TransactionRecordDto;
import com.example.demo.domain.models.EmailText;
import com.example.demo.domain.models.TransactionRecord;
import com.example.demo.domain.persistence.TransactionRecordPersistence;
import com.example.demo.domain.persistence.UserPersistence;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TransactionRecordService {
    private final TransactionRecordPersistence transactionRecordPersistence;
    private final RandomStringService randomStringService;
    private final UserPersistence userPersistence;
    private final EmailService emailService;

    @Autowired
    public TransactionRecordService(TransactionRecordPersistence transactionRecordPersistence,
                                    RandomStringService randomStringService,
                                    UserPersistence userPersistence,
                                    EmailService emailService){
        this.transactionRecordPersistence = transactionRecordPersistence;
        this.randomStringService = randomStringService;
        this.userPersistence = userPersistence;
        this.emailService = emailService;
    }

    public TransactionRecord create(TransactionRecordDto transactionRecordData){
        TransactionRecord transactionRecord = new TransactionRecord();
        BeanUtils.copyProperties(transactionRecordData,transactionRecord);
        transactionRecord.setReference(randomStringService.generateRandomString(12));
        transactionRecord.setTimestampTime(LocalDateTime.now());
        TransactionRecord transactionRecord1 = this.transactionRecordPersistence.create(transactionRecord);
        if (this.userPersistence.read(transactionRecordData.getTelephone()).getSetting().getEmailWhenSuccessfulTransaction()){
            this.sendEmail(transactionRecord);
        }
        return transactionRecord1;
    }

    public void sendEmail(TransactionRecord transactionRecord){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String content = "<h2>Order Detail，</h2>" +
                "<p><strong>Reference:</strong> " + transactionRecord.reference + "</p>" +
                "<p><strong>Telephone:</strong> " + transactionRecord.telephone + "</p>" +
                "<p><strong>Sum of money:</strong> " + transactionRecord.amount + "€</p>" +
                "<p><strong>Order name:</strong> " + transactionRecord.purpose + "</p>" ;
        if(transactionRecord.getTransactionDetails()!=null){
            content += "<p><strong>User name :</strong> " + transactionRecord.getTransactionDetails().firstName + transactionRecord.getTransactionDetails().lastName + "</p>" +
                    "<p><strong>Address:</strong> " + transactionRecord.getTransactionDetails().billingAddress+ "</p>" +
                    "<p><strong>Postcode:</strong> " + transactionRecord.getTransactionDetails().postalCode + "</p>";
        }
        content +="<p><strong>Time:</strong> " + transactionRecord.getTimestampTime().format(formatter) + "</p>";
        String title = "Transaction Successful Detail";
        String emailText = EmailText.builder().content(content).title(title).build().getEmailText();
        String to = this.userPersistence.read(transactionRecord.telephone).getEmail();
        String subject = "Transaction Successful Details";
        emailService.sendEmail(to,subject,emailText);
    }

    public TransactionRecord readByReference(String reference){
        return this.transactionRecordPersistence.readByReference(reference);
    }

    public List<TransactionRecord> readByTelephone(String telephone){
        return this.transactionRecordPersistence.readByTelephone(telephone);
    }
}