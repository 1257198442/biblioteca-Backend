package com.example.demo.domain.service;

import com.example.demo.adapters.rest.dto.TransactionRecordDto;
import com.example.demo.domain.exceptions.LockedResourceException;
import com.example.demo.domain.models.*;
import com.example.demo.domain.persistence.BookPersistence;
import com.example.demo.domain.persistence.LendingDataPersistence;
import com.example.demo.domain.persistence.ReturnDataPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReturnDataService {
    private final ReturnDataPersistence returnDataPersistence;
    private final LendingDataPersistence lendingDataPersistence;
    private final BookPersistence bookPersistence;
    private final TransactionRecordService transactionRecordService;

    @Autowired
    public ReturnDataService(ReturnDataPersistence returnDataPersistence,
                             BookPersistence bookPersistence,
                             LendingDataPersistence lendingDataPersistence,
                             TransactionRecordService transactionRecordService){
        this.returnDataPersistence = returnDataPersistence;
        this.bookPersistence = bookPersistence;
        this.lendingDataPersistence = lendingDataPersistence;
        this.transactionRecordService = transactionRecordService;
    }

    public ReturnData create(String reference){
        LendingData lending = this.lendingDataPersistence.read(reference);
        if(lending.getStatus()){
            throw new LockedResourceException("This books have been returned.");
        }
        Book book = this.bookPersistence.read(lending.getBook().getBookID());
        User user = lending.getUser();
        if(lending.getReference()!=null){
            lending.setStatus(true);
            if(book.getStatus().equals(BookStatus.OCCUPIED)){
                bookPersistence.changeStatus(book.getBookID());
            }
            this.lendingDataPersistence.update(lending);
            createTransactionRecord(lending);
        }
        return this.returnDataPersistence.create(ReturnData.builder()
                .reference(lending.getReference())
                .book(book)
                .user(user)
                .restitutionTime(LocalDateTime.now())
                .lendingTime(lending.getLendingTime())
                .limitTime(lending.getLimitTime())
                .build());
    }

    public ReturnData read(String reference){
        ReturnData restitution = this.returnDataPersistence.read(reference);
        restitution.setUser(restitution.getUser().soloShowNameAndTelephone());
        return restitution;
    }
    public List<ReturnData> readAll(){return this.returnDataPersistence.readAll().stream()
            .map(this::restitutionToRestitutionByShow)
            .collect(Collectors.toList());}

    public ReturnData restitutionToRestitutionByShow(ReturnData restitution){
        restitution.setBook(restitution.getBook().toShowOmit());
        restitution.setUser(restitution.getUser().toShowOmit());
        return restitution;
    }

    public void createTransactionRecord(LendingData lending){
        transactionRecordService.create(
                TransactionRecordDto
                        .builder()
                        .telephone(lending.getUser().getTelephone())
                        .amount(lending.getBook().getDeposit())
                        .purpose("Borrow book(ID:" + lending.getBook().getBookID() + "). For more information, see Returned receipts.")
                        .build());
    }

}
