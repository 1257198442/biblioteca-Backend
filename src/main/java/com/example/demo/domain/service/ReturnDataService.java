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
        LendingData lendingData = this.lendingDataPersistence.read(reference);
        if(lendingData.getStatus()){
            throw new LockedResourceException("This books have been returned.");
        }
        lendingData.setStatus(true);
        this.lendingDataPersistence.update(lendingData);
        createTransactionRecord(lendingData);
        Book book = this.bookPersistence.read(lendingData.getBook().getBookID());
        ReturnData returnData = this.returnDataPersistence.create(ReturnData.builder()
                .reference(lendingData.getReference())
                .book(book)
                .user(lendingData.getUser())
                .restitutionTime(LocalDateTime.now())
                .lendingTime(lendingData.getLendingTime())
                .limitTime(lendingData.getLimitTime())
                .build());
        returnData.setUser(returnData.getUser().soloShowNameAndTelephone());
        return returnData;
    }

    public ReturnData read(String reference){
        ReturnData restitution = this.returnDataPersistence.read(reference);
        restitution.setUser(restitution.getUser().soloShowNameAndTelephone());
        return restitution;
    }

    public List<ReturnData> readAll(){return this.returnDataPersistence.readAll().stream()
            .map(this::restitutionToRestitutionByShow)
            .collect(Collectors.toList());
    }

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

    public ReturnData bookIsReturn(String reference){
        ReturnData returnData = this.returnDataPersistence.read(reference);
        if(returnData.getReturnStatus().equals(ReturnStatus.IS_RETURN)){
            throw new LockedResourceException("bookId"+returnData.getBook().getBookID()+" has been returned.");
        }
        returnData.setReturnStatus(ReturnStatus.IS_RETURN);
        if(this.bookStatusIsOCCUPIED(returnData.getBook().getBookID())){
            this.bookPersistence.changeStatus(returnData.getBook().getBookID());
        }
        return this.returnDataPersistence.update(returnData);
    }

    public ReturnData bookIsNoReturn(String reference){
        LendingData lendingData = this.lendingDataPersistence.read(reference);
        lendingData.setStatus(false);
        this.lendingDataPersistence.update(lendingData);
        ReturnData returnData = this.returnDataPersistence.read(reference);
        returnData.setReturnStatus(ReturnStatus.NO_RETURN);
        return this.userSoloShowNameAndTelephone(this.returnDataPersistence.update(returnData));
    }

    public Boolean bookStatusIsOCCUPIED(String bookId){
        Book bookEnDatabase = this.bookPersistence.read(bookId);
        return bookEnDatabase.getStatus()==BookStatus.OCCUPIED;
    }

    public ReturnData userSoloShowNameAndTelephone(ReturnData returnData){
        returnData.setUser(returnData.getUser().soloShowNameAndTelephone());
        return returnData;
    }

}