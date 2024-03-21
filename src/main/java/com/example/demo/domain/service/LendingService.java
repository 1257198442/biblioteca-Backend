package com.example.demo.domain.service;


import com.example.demo.adapters.rest.dto.LendingUploadDto;
import com.example.demo.adapters.rest.dto.TransactionRecordDto;
import com.example.demo.domain.exceptions.ConflictException;
import com.example.demo.domain.exceptions.LockedResourceException;
import com.example.demo.domain.exceptions.UnprocessableEntityException;
import com.example.demo.domain.models.Book;
import com.example.demo.domain.models.BookStatus;
import com.example.demo.domain.models.Lending;
import com.example.demo.domain.models.User;
import com.example.demo.domain.persistence.BookPersistence;
import com.example.demo.domain.persistence.LendingPersistence;
import com.example.demo.domain.persistence.UserPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LendingService {
    private final LendingPersistence lendingPersistence;
    private final BookPersistence bookPersistence;
    private final UserPersistence userPersistence;
    private final TransactionRecordService transactionRecordService;
    private final RandomStringService randomStringService;
    private final EmailService emailService;
    @Autowired
    public LendingService(LendingPersistence lendingPersistence,
                          BookPersistence bookPersistence,
                          UserPersistence userPersistence,
                          TransactionRecordService transactionRecordService,
                          RandomStringService randomStringService,
                          EmailService emailService){
        this.lendingPersistence = lendingPersistence;
        this.bookPersistence = bookPersistence;
        this.userPersistence = userPersistence;
        this.transactionRecordService = transactionRecordService;
        this.randomStringService = randomStringService;
        this.emailService = emailService;
    }

    public Lending create(LendingUploadDto lendingData){
        Book book = this.bookPersistence.read(lendingData.getBookId());
        User user = this.userPersistence.read(lendingData.getTelephone());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            this.limitTimeIsToLong(LocalDateTime.parse(lendingData.getLimitTime(), formatter));
            if(book.getStatus().equals(BookStatus.ENABLE)){
                transactionRecordService.create(TransactionRecordDto
                        .builder()
                        .telephone(user.getTelephone())
                        .amount(book.getDeposit().negate())
                        .purpose("Borrow book(ID:"+book.getBookID()+")")
                        .build());
                this.bookPersistence.changeStatus(book.getBookID());
                Lending lending = this.lendingPersistence.create(Lending.builder()
                        .user(user)
                        .lendingTime(LocalDateTime.now())
                        .limitTime(LocalDateTime.parse(lendingData.getLimitTime(), formatter))
                        .book(book)
                        .reference(this.randomStringService.generateRandomString(12))
                        .status(false)
                        .build());
                this.sendCreatLendingEmail(lending);
                lending.setUser(lending.getUser().soloShowNameAndTelephone());
                return lending;
            }else if(book.getStatus().equals(BookStatus.DISABLE)){
                throw new LockedResourceException("This book["+book.getBookID()+"] is not currently on the shelves");
            }else {
                throw new ConflictException("The: "+book.getBookID()+" has been checked out");
            }
    }

    public Lending read(String reference){
        Lending lending = this.lendingPersistence.read(reference);
        lending.setUser(lending.getUser().soloShowNameAndTelephone());
        return lending;
    }

    public List<Lending> readAll(){
        return this.lendingPersistence.readAll().stream()
                .map(this::LendingToLendingByShow)
                .collect(Collectors.toList());
    }

    public Lending LendingToLendingByShow(Lending lending){
        lending.setUser(lending.getUser().toShowOmit());
        return lending;
    }

    public List<Lending> readAllByUserTelephone(String telephone){
        return this.lendingPersistence.readByUserTelephone(telephone).stream()
                .map(this::LendingToLendingByShow)
                .collect(Collectors.toList());
    }

    public void limitTimeIsToLong(LocalDateTime time){
        if(time.isAfter(LocalDateTime.now().plusMonths(3))){
            throw new UnprocessableEntityException("LimitTime is over three months.");
        }
    }

    public void sendCreatLendingEmail(Lending lending){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String emailText = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<title>Book Borrowing Order</title>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; }" +
                ".order-details { border: 1px solid #ccc; padding: 20px; width: 500px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='order-details'>" +
                "<h2>Borrowing Orders Detail </h2>" +
                "<p><strong>Reference:</strong> " + lending.getReference() + "</p>" +
                "<p><strong>Deposit:</strong> " + lending.getBook().getDeposit() + "€</p>" +
                "<p><strong>BookID:</strong> " + lending.getBook().getBookID()+ "</p>" +
                "<p><strong>Book Name:</strong> " + lending.getBook().getName() + "</p>" +
                "<p><strong>User name :</strong> " + lending.getUser().getName() + "</p>" +
                "<p><strong>Telephone:</strong> " + lending.getUser().getTelephone()+ "</p>" +
                "<p><strong>borrowing Time:</strong> " + lending.getLendingTime().format(formatter) + "</p>"+
                "<p><strong>Duration:</strong> " + lending.getLimitTime().format(formatter) + "</p>"+
                "<p><strong>Please return the book before the return deadline to avoid extra charges for late return.</strong></p>"+
                "</div>" +
                "</body>" +
                "</html>";
        String to = this.userPersistence.read(lending.getUser().getTelephone()).getEmail();
        String subject = "Book Borrowing Order Details";
        if(lending.getUser().getSetting().getEmailWhenOrderIsPaid()){
            emailService.sendEmail(to,subject,emailText);
        }
    }
}