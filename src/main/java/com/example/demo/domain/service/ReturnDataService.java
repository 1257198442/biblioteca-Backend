package com.example.demo.domain.service;

import com.example.demo.adapters.rest.dto.BookDamageDegreeDto;
import com.example.demo.adapters.rest.dto.TransactionRecordDto;
import com.example.demo.domain.exceptions.LockedResourceException;
import com.example.demo.domain.models.*;
import com.example.demo.domain.persistence.BookPersistence;
import com.example.demo.domain.persistence.LendingDataPersistence;
import com.example.demo.domain.persistence.ReturnDataPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
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
        ReturnData returnData;
        LendingData lendingData = this.lendingDataPersistence.read(reference);
        if(lendingData.getStatus()){
            throw new LockedResourceException("This books have been returned.");
        }
        lendingData.setStatus(true);
        if(returnDataPersistence.existReference(reference)){
            returnData = this.returnDataPersistence.read(reference);
            this.isReturn(returnData);
            this.returnDataPersistence.update(returnData);
        }else {
            returnData = this.generateReturnData(lendingData);
            returnDataPersistence.create(returnData);
        }

        this.lendingDataPersistence.update(lendingData);
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

    public ReturnData bookIsReturn(String reference, BookDamageDegreeDto bookDamageDegreeData){
        ReturnData returnData = this.returnDataPersistence.read(reference);
        this.isReturn(returnData);
        returnData.setReturnStatus(ReturnStatus.IS_RETURN);
        if(this.bookStatusIsOCCUPIED(returnData.getBook().getBookID())){
            this.bookPersistence.changeStatus(returnData.getBook().getBookID());
        }
        returnData.setBook(this.bookPersistence.read(returnData.getBook().getBookID()));
        return this.userSoloShowNameAndTelephone(this.returnDataPersistence.update(this.generateReturnDataInformation(returnData,bookDamageDegreeData)));
    }

    public ReturnData bookIsNoReturn(String reference){
        ReturnData returnData = this.returnDataPersistence.read(reference);
        this.isReturn(returnData);
        LendingData lendingData = this.lendingDataPersistence.read(reference);
        lendingData.setStatus(false);
        this.lendingDataPersistence.update(lendingData);
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

    public List<ReturnData> readAllByUserTelephone(String telephone){
        return this.returnDataPersistence.readByUserTelephone(telephone).stream()
                .map(this::restitutionToRestitutionByShow).collect(Collectors.toList());
    }

    public List<ReturnData> readAllByWaitingForVerification(){
        return this.returnDataPersistence
                .readAll()
                .stream()
                .filter(restitution -> restitution.getReturnStatus()==ReturnStatus.WAITING_FOR_VERIFICATION)
                .map(this::restitutionToRestitutionByShow)
                .collect(Collectors.toList());
    }

    private ReturnData generateReturnData(LendingData lendingData){
        return ReturnData.builder()
                .reference(lendingData.getReference())
                .book(this.bookPersistence.read(lendingData.getBook().getBookID()))
                .user(lendingData.getUser())
                .returnTime(LocalDateTime.now())
                .lendingTime(lendingData.getLendingTime())
                .limitTime(lendingData.getLimitTime())
                .returnStatus(ReturnStatus.WAITING_FOR_VERIFICATION)
                .build();
    }

    private ReturnData generateReturnDataInformation(ReturnData returnData, BookDamageDegreeDto bookDamageDegreeData) {
        BigDecimal percentagePostponement = BigDecimal.valueOf(0);
        BigDecimal percentageAppearance;
        BigDecimal totalAppearance;
        BigDecimal amount;
        long days=0;
        if (returnData.getLimitTime().isBefore(returnData.getReturnTime())) {
            days= computationalTime(returnData);
            percentagePostponement=this.computationalPercentagePostponement(days);
        }
        percentageAppearance = Degree.percentageAppearance(Degree.fromString(bookDamageDegreeData.getDegree()));
        totalAppearance = percentagePostponement.add(percentageAppearance);
        amount =this.computationalAmount(returnData.getBook().getDeposit(),BigDecimal.valueOf(1).subtract(totalAppearance));
        this.createTransactionRecord(returnData,amount);
        returnData.setBookDamageDegree(this.generateBookDamageDegree(bookDamageDegreeData,days,percentagePostponement,percentageAppearance,totalAppearance,amount));
        return returnData;
    }

    public long computationalTime(ReturnData returnData){
        Duration duration = Duration.between(returnData.getLimitTime(),returnData.getReturnTime());
        long days = duration.toDaysPart();
        if (duration.toNanos() % Duration.ofDays(1).toNanos() > 0) {
            days++;
        }
        return days;
    }

    public BigDecimal computationalPercentagePostponement(long days){
        if (days < 16) {
            return BigDecimal.valueOf(0.05).multiply(BigDecimal.valueOf(days)) ;
        } else if(days <30 && days >= 16){
            return BigDecimal.valueOf(0.8);
        } else {
            return BigDecimal.valueOf(1.0);
        }
    }

    private void createTransactionRecord(ReturnData returnData,BigDecimal amount){
        transactionRecordService.create(
                TransactionRecordDto
                        .builder()
                        .telephone(returnData.getUser().getTelephone())
                        .amount(amount)
                        .purpose("Borrow book(ID:" + returnData.getBook().getBookID() + "). For more information, see Returned receipts.")
                        .build());
    }

    private BigDecimal computationalAmount(BigDecimal bookDeposit, BigDecimal percentage){
        return bookDeposit.multiply(percentage.compareTo(BigDecimal.valueOf(0))>=0?percentage:BigDecimal.valueOf(0))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BookDamageDegree generateBookDamageDegree(BookDamageDegreeDto bookDamageDegreeData
            , long days
            , BigDecimal percentagePostponement
            , BigDecimal percentageAppearance
            , BigDecimal totalAppearance
            , BigDecimal amount){
        return BookDamageDegree.builder()
                .degree(Degree.fromString(bookDamageDegreeData.getDegree()))
                .addendum(bookDamageDegreeData.getAddendum())
                .detailsDeductions("Extended by "+days+" day, deduction of "
                        +percentagePostponement+". Degree of book integrity ["
                        +bookDamageDegreeData.getDegree()+"], deduction of "
                        +percentageAppearance+", total deduction of "
                        +totalAppearance+". Hence the return of the deposit "
                        +amount+"€")
                .returnDeposit(amount)
                .time(LocalDateTime.now())
                .build();
    }

    public void isReturn(ReturnData returnData){
        if(returnData.getReturnStatus().equals(ReturnStatus.IS_RETURN)){
            throw new LockedResourceException("bookId"+returnData.getBook().getBookID()+" has been returned.");
        }
    }

}