package com.example.demo.domain.service;

import com.example.demo.adapters.rest.dto.TransactionRecordDto;
import com.example.demo.domain.models.TransactionRecord;
import com.example.demo.domain.persistence.TransactionRecordPersistence;
import com.example.demo.domain.persistence.UserPersistence;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
        if (this.userPersistence.read(transactionRecordData.getTelephone()).getSetting().getEmailWhenOrderIsGenerated()){
            this.sendEmail(transactionRecord);
        }
        return transactionRecord1;
    }

    public void sendEmail(TransactionRecord transactionRecord){
        String emailText = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<title>Order Detail</title>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; }" +
                ".order-details { border: 1px solid #ccc; padding: 20px; width: 500px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='order-details'>" +
                "<h2>Order Detail，</h2>" +
                "<p><strong>Reference:</strong> " + transactionRecord.reference + "</p>" +
                "<p><strong>Telephone:</strong> " + transactionRecord.telephone + "</p>" +
                "<p><strong>Sum of money:</strong> " + transactionRecord.amount + "€</p>" +
                "<p><strong>Order name:</strong> " + transactionRecord.purpose + "</p>" ;
        if(transactionRecord.getTransactionDetails()!=null){
            emailText += "<p><strong>User name :</strong> " + transactionRecord.getTransactionDetails().firstName + transactionRecord.getTransactionDetails().lastName + "</p>" +
                    "<p><strong>Address:</strong> " + transactionRecord.getTransactionDetails().billingAddress+ "</p>" +
                    "<p><strong>Postcode:</strong> " + transactionRecord.getTransactionDetails().postalCode + "</p>";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        emailText +="<p><strong>Time:</strong> " + transactionRecord.getTimestampTime().format(formatter) + "</p>"+
                "</div>" +
                "</body>" +
                "</html>";
        String to = this.userPersistence.read(transactionRecord.telephone).getEmail();
        String subject = "Order Details";
        emailService.sendEmail(to,subject,emailText);
    }

    public TransactionRecord readByReference(String reference){
        return this.transactionRecordPersistence.readByReference(reference);
    }

    public List<TransactionRecord> readByTelephone(String telephone){
        return this.transactionRecordPersistence.readByTelephone(telephone);
    }
}