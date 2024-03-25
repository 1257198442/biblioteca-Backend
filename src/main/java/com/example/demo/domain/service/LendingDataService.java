package com.example.demo.domain.service;


import com.example.demo.adapters.rest.dto.LendingDataUploadDto;
import com.example.demo.adapters.rest.dto.TransactionRecordDto;
import com.example.demo.adapters.rest.show.LendingStatisticsShow;
import com.example.demo.domain.exceptions.ConflictException;
import com.example.demo.domain.exceptions.LockedResourceException;
import com.example.demo.domain.exceptions.UnprocessableEntityException;
import com.example.demo.domain.models.*;
import com.example.demo.domain.persistence.BookPersistence;
import com.example.demo.domain.persistence.LendingDataPersistence;
import com.example.demo.domain.persistence.UserPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class LendingDataService {
    private final LendingDataPersistence lendingDataPersistence;
    private final BookPersistence bookPersistence;
    private final UserPersistence userPersistence;
    private final TransactionRecordService transactionRecordService;
    private final RandomStringService randomStringService;
    private final EmailService emailService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Autowired
    public LendingDataService(LendingDataPersistence lendingDataPersistence,
                              BookPersistence bookPersistence,
                              UserPersistence userPersistence,
                              TransactionRecordService transactionRecordService,
                              RandomStringService randomStringService,
                              EmailService emailService){
        this.lendingDataPersistence = lendingDataPersistence;
        this.bookPersistence = bookPersistence;
        this.userPersistence = userPersistence;
        this.transactionRecordService = transactionRecordService;
        this.randomStringService = randomStringService;
        this.emailService = emailService;
    }

    public LendingData create(LendingDataUploadDto lendingDataUploadDto){
        Book book = this.bookPersistence.read(lendingDataUploadDto.getBookId());
        User user = this.userPersistence.read(lendingDataUploadDto.getTelephone());
        this.limitTimeIsToLong(LocalDateTime.parse(lendingDataUploadDto.getLimitTime(), formatter));
        if(book.getStatus().equals(BookStatus.ENABLE)){
            crateTransactionRecord(book,user);
            this.bookPersistence.changeStatus(book.getBookID());
            LendingData lending = this.createLendingData(lendingDataUploadDto,user,book);
            this.sendCreatLendingEmail(lending);
            return this.userSoloShowNameAndTelephone(lending);
        }
        if(book.getStatus().equals(BookStatus.DISABLE)){
            throw new LockedResourceException("This book["+book.getBookID()+"] is not currently on the shelves");
        }
        throw new ConflictException("The: "+book.getBookID()+" has been checked out");
    }

    public LendingData read(String reference){
        LendingData lendingData = this.lendingDataPersistence.read(reference);
        this.userSoloShowNameAndTelephone(lendingData);
        return lendingData;
    }

    public List<LendingData> readAll(){
        return this.lendingDataPersistence.readAll().stream()
                .map(this::LendingToLendingByShow)
                .collect(Collectors.toList());
    }

    public LendingData LendingToLendingByShow(LendingData lending){
        lending.setUser(lending.getUser().toShowOmit());
        return lending;
    }

    public List<LendingData> readAllByUserTelephone(String telephone){
        return this.lendingDataPersistence.readByUserTelephone(telephone).stream()
                .map(this::LendingToLendingByShow)
                .map(this::userSoloShowNameAndTelephone)
                .collect(Collectors.toList());
    }

    public List<LendingData> readNoReturnByTelephone(String telephone){
        return this.lendingDataPersistence.readByUserTelephone(telephone).stream()
                .filter(lending -> !lending.getStatus()).collect(Collectors.toList()).stream()
                .map(this::LendingToLendingByShow)
                .collect(Collectors.toList());
    }

    public LendingStatisticsShow getLendingStatistics(){
        List<LendingData> allLendingData = this.lendingDataPersistence.readAll();
        return LendingStatisticsShow.builder()
                .all(allLendingData.size())
                .thisYear(getStatistics(allLendingData,LocalDate.now().withDayOfYear(1),LocalDate.now().withDayOfYear(LocalDate.now().lengthOfYear())))
                .thisMonth(getStatistics(allLendingData,LocalDate.now().withDayOfMonth(1),LocalDate.now().withDayOfMonth(LocalDateTime.now().toLocalDate().lengthOfMonth())))
                .thisWeek(getStatistics(allLendingData,LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)),LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY))))
                .today((int) allLendingData.stream().filter(lending -> lending.getLendingTime().toLocalDate().equals(LocalDateTime.now().toLocalDate())).count())
                .build();
    }

    private int getStatistics(List<LendingData> data,LocalDate time1, LocalDate time2){
        return (int)data.stream()
                .filter(lending -> lending.getLendingTime().toLocalDate().isAfter(time1.minusDays(1)) && lending.getLendingTime().toLocalDate().isBefore(time2.plusDays(1))).count();
    }

    public List<Integer> getLendingMonthlyCountsByThisYear(){
        int currentMonth = YearMonth.now().getMonthValue();
        Stream<LendingData> lendingThisYear = this.lendingDataPersistence.readAll().stream()
                .filter(lending -> lending.getLendingTime().getYear() == YearMonth.now().getYear());
        Map<Month, Long> lendingByMonth = lendingThisYear
                .collect(Collectors.groupingBy(lending -> lending.getLendingTime().getMonth(), Collectors.counting()));
        return IntStream.rangeClosed(1, currentMonth)
                .mapToObj(month -> lendingByMonth.getOrDefault(Month.of(month), 0L).intValue())
                .collect(Collectors.toList());
    }

    public List<Integer> getLendingDailyCountsByThisWeek() {
        int numOfDaysInWeek;
        LocalDate today = LocalDate.now();
        numOfDaysInWeek=today.getDayOfWeek().getValue();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).minusDays(1);
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).plusDays(1);
        Stream<LendingData> lendingThisWeek = this.lendingDataPersistence.readAll().stream()
                .filter(lending -> lending.getLendingTime().toLocalDate().isAfter(startOfWeek) && lending.getLendingTime().toLocalDate().isBefore(endOfWeek));
        Map<DayOfWeek, Long> lendingByDay = lendingThisWeek
                .collect(Collectors.groupingBy(lending -> lending.getLendingTime().getDayOfWeek(), Collectors.counting()
                ));
        return IntStream.rangeClosed(1, numOfDaysInWeek)
                .mapToObj(dayOfWeek -> lendingByDay.getOrDefault(DayOfWeek.of(dayOfWeek), 0L).intValue())
                .collect(Collectors.toList());
    }



    public List<Integer> getLendingYearlyCounts() {
        int earliestYear = this.lendingDataPersistence.readAll().stream()
                .mapToInt(lending -> lending.getLendingTime().getYear())
                .min()
                .orElse(Year.now().getValue());
        int currentYear = Year.now().getValue();
        List<Integer> yearlyCounts = new ArrayList<>();
        for (int year = earliestYear; year <= currentYear; year++) {
            int finalYear = year;
            long lendingCount = this.lendingDataPersistence.readAll().stream()
                    .filter(lending -> lending.getLendingTime().getYear() == finalYear)
                    .count();
            yearlyCounts.add((int) lendingCount);
        }
        return yearlyCounts;
    }

    public List<LendingData> sendEmailToUserByApproachingExpiryDate(){
        Stream<LendingData> lendingData = this.lendingDataPersistence.readAllByNoReturn()
                .stream()
                .filter(lending -> !lending.getReturnSendEmail().getReturnReminderEmails())
                .filter(lending -> lending.getLimitTime().toLocalDate().isBefore(LocalDate.now().plusDays(3)))
                .filter(lending -> lending.getLimitTime().toLocalDate().isAfter(LocalDate.now()));
        return lendingData
                .map(lending -> {
                    this.sendApproachingExpiryDateEmail(lending);
                    ReturnSendEmail returnSendEmail = lending.getReturnSendEmail();
                    returnSendEmail.setReturnReminderEmails(true);
                    lending.setReturnSendEmail(returnSendEmail);
                    return this.lendingDataPersistence.update(lending);
                }).map(this::LendingToLendingByShow)
                .collect(Collectors.toList());
    }

    public List<LendingData> sendEmailToUserByOrderOverdue(){
        Stream<LendingData> lendingData =this.lendingDataPersistence.readAllByNoReturn()
                .stream()
                .filter(lending -> !lending.getReturnSendEmail().getReturnOverdueReminderEmails())
                .filter(lending -> lending.getLimitTime().isBefore(LocalDateTime.now()));
        return lendingData.map(lending -> {
                    this.sendOverdueEmail(lending);
                    ReturnSendEmail returnSendEmail = lending.getReturnSendEmail();
                    returnSendEmail.setReturnOverdueReminderEmails(true);
                    lending.setReturnSendEmail(returnSendEmail);
                    return this.lendingDataPersistence.update(lending);})
                .map(this::LendingToLendingByShow)
                .collect(Collectors.toList());
    }

    public List<LendingData> sendEmailToUserByExtensionBeyond30Days(){
        Stream<LendingData> lendingData = this.readLendingDataByExtensionBeyond30Days()
                .filter(lending -> !lending.getReturnSendEmail().getReturnOverdueBeyond30DayReminderEmails());
        return lendingData.map(lending -> {
                    this.sendBeyond30DaysEmail(lending);
                    ReturnSendEmail returnSendEmail = lending.getReturnSendEmail();
                    returnSendEmail.setReturnOverdueBeyond30DayReminderEmails(true);
                    lending.setReturnSendEmail(returnSendEmail);
                    return this.lendingDataPersistence.update(lending);})
                .map(this::Ban)
                .collect(Collectors.toList());
    }

    public Stream<LendingData> readLendingDataByExtensionBeyond30Days(){
        return this.lendingDataPersistence.readAllByNoReturn()
                .stream()
                .filter(lending -> lending.getLimitTime().toLocalDate().isBefore(LocalDate.now().minusDays(30)))
                .map(this::LendingToLendingByShow);
    }

    public LendingData Ban(LendingData lendingData){
        User users = lendingData.getUser();
        if(!users.getRole().equals(Role.BAN)){
            users.setRole(Role.BAN);
        }
        return lendingData;
    }

    private void crateTransactionRecord(Book book,User user){
        transactionRecordService.create(TransactionRecordDto
                .builder()
                .telephone(user.getTelephone())
                .amount(book.getDeposit().negate())
                .purpose("Borrow book(ID:"+book.getBookID()+")")
                .build());
    }

    private LendingData createLendingData(LendingDataUploadDto lendingDataUploadDto,User user,Book book){
        return this.lendingDataPersistence.create(LendingData.builder()
                .user(user)
                .lendingTime(LocalDateTime.now())
                .limitTime(LocalDateTime.parse(lendingDataUploadDto.getLimitTime(), formatter))
                .book(book)
                .reference(this.randomStringService.generateRandomString(12))
                .status(false)
                .returnSendEmail(new ReturnSendEmail().init())
                .build());
    }

    private void limitTimeIsToLong(LocalDateTime time){
        if(time.isAfter(LocalDateTime.now().plusMonths(3))){
            throw new UnprocessableEntityException("LimitTime is over three months.");
        }
    }

    private void sendCreatLendingEmail(LendingData lendingData){
        String title = "Borrowing Orders Detail";
        String content = "<h2>Borrowing Orders Detail </h2>" +
                "<p><strong>Reference:</strong> " + lendingData.getReference() + "</p>" +
                "<p><strong>Deposit:</strong> " + lendingData.getBook().getDeposit() + "€</p>" +
                "<p><strong>BookID:</strong> " + lendingData.getBook().getBookID()+ "</p>" +
                "<p><strong>Book Name:</strong> " + lendingData.getBook().getName() + "</p>" +
                "<p><strong>User name :</strong> " + lendingData.getUser().getName() + "</p>" +
                "<p><strong>Telephone:</strong> " + lendingData.getUser().getTelephone()+ "</p>" +
                "<p><strong>borrowing Time:</strong> " + lendingData.getLendingTime().format(formatter) + "</p>"+
                "<p><strong>Duration:</strong> " + lendingData.getLimitTime().format(formatter) + "</p>"+
                "<p><strong>Please return the book before the return deadline to avoid extra charges for late return.</strong></p>";
        String subject = "Book Borrowing Order Details";
        if(lendingData.getUser().getSetting().getEmailWhenOrderIsPaid()){
            emailService.sendEmail(getEmailAddress(lendingData),subject,EmailText.builder().title(title).content(content).build().getEmailText());
        }
    }

    private void sendApproachingExpiryDateEmail(LendingData lendingData){
        String title = "Book return reminder";
        String content = "<h2>Book return reminder</h2>" +
                "<p><strong>Reference:</strong> " + lendingData.getReference() + "</p>" +
                "<p><strong>Book ID:</strong> " + lendingData.getBook().getBookID() + "</p>" +
                "<p><strong>Duration:</strong> " + lendingData.getLimitTime().format(formatter) + "</p>" +
                "<p><strong>You have a book "+lendingData.getBook().getName()+" that will expire on "+lendingData.getLimitTime().format(formatter) +
                ", to avoid incurring additional fees, please return this book before the specified time. Thank you!.</strong></p>";
        String subject = "Book return reminder";
        User user = this.userPersistence.read(lendingData.getUser().getTelephone());
        if(user.getSetting().getEmailWhenOrdersAboutToExpire()){
            emailService.sendEmail(getEmailAddress(lendingData),subject,this.generateEmail(title,content));
        }
    }

    public void sendOverdueEmail(LendingData lendingData){
        String title = "Order expiry reminder";
        String content = "<h2>Order expiry reminder</h2>" +
                "<p><strong>Reference:</strong> " + lendingData.getReference() + "</p>" +
                "<p><strong>Book ID:</strong> " + lendingData.getBook().getBookID() + "</p>" +
                "<p><strong>Duration:</strong> " + lendingData.getLimitTime().format(formatter) + "</p>" +
                "<p><strong>You have a book "+lendingData.getBook().getName()+"expired on "+lendingData.getLimitTime().format(formatter) +
                "To avoid incurring more extra charges, please return this book as soon as possible. Thanks!.</strong></p>";
        String subject = "Book return reminder";
        emailService.sendEmail(getEmailAddress(lendingData),subject,this.generateEmail(title,content));
    }

    public void sendBeyond30DaysEmail(LendingData lendingData){
        String title = "Account Warning";
        String content = "<h2>Account Warning</h2>" +
                "<p><strong>Reference:</strong> " + lendingData.getReference() + "</p>" +
                "<p><strong>Book ID:</strong> " + lendingData.getBook().getBookID() + "</p>" +
                "<p><strong>borrowing Time:</strong> " + lendingData.getLendingTime().format(formatter) + "</p>"+
                "<p><strong>Duration:</strong> " + lendingData.getLimitTime().format(formatter) + "</p>" +
                "<p><strong>Your account "+lendingData.getUser().getTelephone()+" has a book "+lendingData.getBook().getName() +
                " that is 30 days overdue, and your account has been frozen. If you need to unfreeze your account, please contact customer service!.</strong></p>";
        String subject = "Account Warning";
        emailService.sendEmail(getEmailAddress(lendingData),subject,this.generateEmail(title,content));
    }

    private LendingData userSoloShowNameAndTelephone(LendingData lendingData){
        lendingData.setUser(lendingData.getUser().soloShowNameAndTelephone());
        return lendingData;
    }

    public String getEmailAddress(LendingData lendingData){
        return this.userPersistence.read(lendingData.getUser().getTelephone()).getEmail();
    }

    public String generateEmail(String title, String content){
        return EmailText.builder().title(title).content(content).build().getEmailText();
    }

}