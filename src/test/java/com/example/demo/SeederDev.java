package com.example.demo;

import com.example.demo.adapters.mongodb.daos.*;
import com.example.demo.adapters.mongodb.entities.*;
import com.example.demo.domain.models.*;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@Profile("dev")
public class SeederDev {
    private final UserRepository userDao;
    private final LibraryRepository libraryDao;
    private final AvatarRepository avatarDao;
    private final WalletRepository walletDao;
    private final TransactionRecordRepository transactionRecordDao;
    private final BookRepository bookDao;
    private final AuthorRepository authorDao;
    private final TypeRepository typeDao;
    private final LendingDataRepository lendingDataDao;
    private final ReturnDataRepository returnDataDao;
    private final CollectionListRepository collectionListDao;

    @Autowired
    public SeederDev(UserRepository userDao,
                     LibraryRepository libraryDao,
                     AvatarRepository avatarDao,
                     WalletRepository walletDao,
                     TransactionRecordRepository transactionRecordDao,
                     BookRepository bookDao,
                     AuthorRepository authorDao,
                     TypeRepository typeDao,
                     LendingDataRepository lendingDataDao,
                     ReturnDataRepository returnDataDao,
                     CollectionListRepository collectionListDao){
        this.userDao = userDao;
        this.libraryDao = libraryDao;
        this.avatarDao = avatarDao;
        this.walletDao = walletDao;
        this.transactionRecordDao = transactionRecordDao;
        this.bookDao = bookDao;
        this.authorDao = authorDao;
        this.typeDao = typeDao;
        this.lendingDataDao = lendingDataDao;
        this.returnDataDao = returnDataDao;
        this.collectionListDao = collectionListDao;
        this.deleteAllAndInitializeAndSeedDataBase();
    }
    public void deleteAllAndInitializeAndSeedDataBase() {
        this.deleteAllAndInitialize();
        this.seedDataBase();
    }

    public void deleteAllAndInitialize() {
        this.userDao.deleteAll();
        this.libraryDao.deleteAll();
        this.avatarDao.deleteAll();
        this.walletDao.deleteAll();
        this.transactionRecordDao.deleteAll();
        this.bookDao.deleteAll();
        this.authorDao.deleteAll();
        this.typeDao.deleteAll();
        this.lendingDataDao.deleteAll();
        this.returnDataDao.deleteAll();
    }
    private void seedDataBase() {

        LogManager.getLogger(this.getClass()).warn("------- Initial Load from JAVA -----------");
        LogManager.getLogger(this.getClass()).warn("-------      Initial User      -----------");
        String pass = new BCryptPasswordEncoder().encode("6");
        Setting setting = new Setting().init();
        Setting setting1 = Setting.builder().hideMyProfile(true).emailWhenSuccessfulTransaction(false).emailWhenOrderIsPaid(true).emailWhenOrdersAboutToExpire(true).hideMyCollectionList(true).build();
        UserEntity[] userEntities = {
                UserEntity.builder().role(Role.ROOT).createTime(LocalDateTime.now()).email("1257198442@qq.com").name("root").password(pass).telephone("+34666666666").active(true).description("I am is root").birthdays(LocalDate.of(2000,1,1)).setting(setting).build(),
                UserEntity.builder().role(Role.ADMINISTRATOR).createTime(LocalDateTime.now()).email("1257198442@qq.com").name("Administrator").password(pass).telephone("+34666000001").active(true).description("I am is administrator").birthdays(LocalDate.of(2000,1,2)).setting(setting).build(),
                UserEntity.builder().role(Role.CLIENT).createTime(LocalDateTime.now()).email("1257198442@qq.com").name("User").password(pass).telephone("+34666000002").active(true).description("I am is client").birthdays(LocalDate.of(2000,1,3)).setting(setting).build(),
                UserEntity.builder().role(Role.BAN).createTime(LocalDateTime.now()).email("test1@test.com").name("BAN").password(pass).telephone("+34666").active(true).description("An root").birthdays(LocalDate.of(2000,1,4)).setting(setting).build(),
                UserEntity.builder().role(Role.CLIENT).createTime(LocalDateTime.now()).email("test2@test.com").name("User").password(pass).telephone("+34645321068").active(true).description("An root").birthdays(LocalDate.of(2000,1,5)).setting(setting).build(),
                UserEntity.builder().role(Role.CLIENT).createTime(LocalDateTime.now()).email("test2@test.com").name("User").password(pass).telephone("+34123").active(true).description("user").birthdays(LocalDate.of(2000,1,6)).setting(setting1).build(),
                UserEntity.builder().role(Role.CLIENT).createTime(LocalDateTime.now()).email("test2@test.com").name("User").password(pass).telephone("+34321").active(true).description("user").birthdays(LocalDate.of(2000,1,6)).setting(setting1).build(),
                UserEntity.builder().role(Role.CLIENT).createTime(LocalDateTime.now()).email("test2@test.com").name("User").password(pass).telephone("+34990099009").active(true).description("user").birthdays(LocalDate.of(2000,1,6)).setting(setting1).build(),
                UserEntity.builder().role(Role.CLIENT).createTime(LocalDateTime.now()).email("test2@test.com").name("User").password(pass).telephone("+34333333333").active(true).description("user").birthdays(LocalDate.of(2000,1,6)).setting(setting1).build(),

        };
        userDao.saveAll(Arrays.asList(userEntities));

        LogManager.getLogger(this.getClass()).warn("-------      Initial Library      -----------");
        LibraryEntity[] libraryEntities = {
                LibraryEntity.builder()
                        .address("X.º XX XXXXXXX, XX, 28001, XXXXXX, Madrid")
                        .telephone("6669996669")
                        .name("BIBLIOTECA")
                        .email("bibliotecaMadrid@email.com")
                        .postalCode("28001")
                        .businessHours("Monday to Friday 10:00 to 22:00 Saturday and Sunday 10:00 to 16:00")
                        .introduction("this is test introduction")
                        .twitter("https://www.twitter.com/")
                        .googleMail("https://mail.google.com/")
                        .instagram("https://www.instagram.com/")
                        .facebook("https://www.facebook.com/")
                        .discord("https://www.discord.com/").build()
        };
        this.libraryDao.saveAll(Arrays.asList(libraryEntities));

        LogManager.getLogger(this.getClass()).warn("-------      Initial Library      -----------");
        AvatarEntity[] avatarEntities = {
                AvatarEntity.builder().fileName("user.png").url("https://localhost/images/avatar/user.png").telephone("+34666666666").uploadTime(LocalDateTime.now()).build(),
                AvatarEntity.builder().fileName("user.png").url("https://localhost/images/avatar/user.png").telephone("+34666000001").uploadTime(LocalDateTime.now()).build(),
                AvatarEntity.builder().fileName("user.png").url("https://localhost/images/avatar/user.png").telephone("+34666000002").uploadTime(LocalDateTime.now()).build(),
                AvatarEntity.builder().fileName("user.png").url("https://localhost/images/avatar/user.png").telephone("+34666").uploadTime(LocalDateTime.of(2000,1,1,1,1,1)).build(),
                AvatarEntity.builder().fileName("user.png").url("https://localhost/images/avatar/user.png").telephone("+34645321068").uploadTime(LocalDateTime.now()).build(),
                AvatarEntity.builder().fileName("user.png").url("https://localhost/images/avatar/user.png").telephone("+34123").uploadTime(LocalDateTime.now()).build(),
                AvatarEntity.builder().fileName("user.png").url("https://localhost/images/avatar/user.png").telephone("+34321").uploadTime(LocalDateTime.now()).build(),
                AvatarEntity.builder().fileName("user.png").url("https://localhost/images/avatar/user.png").telephone("+34990099009").uploadTime(LocalDateTime.now()).build(),
                AvatarEntity.builder().fileName("user.png").url("https://localhost/images/avatar/user.png").telephone("+34333333333").uploadTime(LocalDateTime.now()).build(),
        };
        this.avatarDao.saveAll(Arrays.asList(avatarEntities));

        LogManager.getLogger(this.getClass()).warn("-------      Initial Wallet      -----------");
        WalletEntity[] walletEntities = {
                WalletEntity.builder().telephone("+34666666666").balance(new BigDecimal("999")).build(),
                WalletEntity.builder().telephone("+34666000001").balance(new BigDecimal("0")).build(),
                WalletEntity.builder().telephone("+34666000002").balance(new BigDecimal("0")).build(),
                WalletEntity.builder().telephone("+34666").balance(new BigDecimal("0")).build(),
                WalletEntity.builder().telephone("+34645321068").balance(new BigDecimal("0")).build(),
                WalletEntity.builder().telephone("+34123").balance(new BigDecimal("235")).build(),
                WalletEntity.builder().telephone("+34321").balance(new BigDecimal("1000000")).build(),
                WalletEntity.builder().telephone("+34990099009").balance(new BigDecimal("1000000")).build(),
                WalletEntity.builder().telephone("+34333333333").balance(new BigDecimal("1000000")).build()
        };
        this.walletDao.saveAll(Arrays.asList(walletEntities));

        LogManager.getLogger(this.getClass()).warn("-------      Initial TransactionRecord      -----------");
        TransactionDetails transactionDetails = TransactionDetails.builder()
                .billingAddress("Address")
                .city("MADRID")
                .firstName("JIAMING")
                .lastName("SHI")
                .postalCode("00000").build();
        TransactionRecordEntity[] transactionRecordEntities = {
                TransactionRecordEntity.builder().telephone("+34123").timestampTime(LocalDateTime.now()).transactionDetails(transactionDetails).reference("ijadlkfjsjf1").purpose("test").amount(new BigDecimal("100")).build(),
                TransactionRecordEntity.builder().telephone("+34123").timestampTime(LocalDateTime.now()).transactionDetails(transactionDetails).reference("asldjaaslskl").purpose("test").amount(new BigDecimal("-100")).build(),
        };
        this.transactionRecordDao.saveAll(Arrays.asList(transactionRecordEntities));

        LogManager.getLogger(this.getClass()).warn("-------      Initial Author      -----------");
        AuthorEntity[] authorEntities = {
                AuthorEntity.builder().authorId("111").imgUrl("https://localhost/images/author/user.png").name("author1").nationality("English").description("test text1").imgFileName("user.png").build(),
                AuthorEntity.builder().authorId("222").imgUrl("https://localhost/images/author/user.png").name("author2").nationality("China").description("test text2").imgFileName("user.png").build(),
                AuthorEntity.builder().authorId("333").imgUrl("https://localhost/images/author/user.png").name("author3-1").nationality("Spain").description("test text3-1").imgFileName("user.png").build(),
                AuthorEntity.builder().authorId("444").imgUrl("https://localhost/images/author/user.png").name("author3-2").nationality("Spain").description("test text3-2").imgFileName("user.png").build(),
                AuthorEntity.builder().authorId("delete").imgUrl("https://localhost/images/author/user.png").name("author4").nationality("Spain").description("test text4").imgFileName("user.png").build(),
                AuthorEntity.builder().authorId("update").imgUrl("https://localhost/images/author/user.png").name("author5").nationality("Spain").description("test text5").imgFileName("user.png").build(),
        };
        this.authorDao.saveAll(Arrays.asList(authorEntities));

        LogManager.getLogger(this.getClass()).warn("-------      Initial Type      -----------");
        TypeEntity[] typeEntities = {
                TypeEntity.builder().description("1").name("test").build(),
                TypeEntity.builder().description("2").name("test2").build(),
                TypeEntity.builder().description("3").name("test3").build(),
                TypeEntity.builder().description("4").name("test4").build(),
                TypeEntity.builder().description("5").name("test5").build(),
        };
        this.typeDao.saveAll(Arrays.asList(typeEntities));

        LogManager.getLogger(this.getClass()).warn("-------      Initial Book      -----------");
        BookEntity[] bookEntities = {
                BookEntity.builder().bookID("1").name("test").entryTime(LocalDateTime.now()).description("this is a test book").status(BookStatus.ENABLE).imgUrl("https://localhost/images/book/default.jpg").imgFileName("default.jpg").deposit(new BigDecimal("12")).language(Language.English).issn("1111111111").authorId(List.of(authorEntities[0].getAuthorId())).bookType(List.of(typeEntities[0].getName())).publisher("test1").barcode("abc").build(),
                BookEntity.builder().bookID("2").name("test1").entryTime(LocalDateTime.now()).description("this is a test book1").status(BookStatus.ENABLE).imgUrl("https://localhost/images/book/default.jpg").imgFileName("default.jpg").deposit(new BigDecimal("32")).language(Language.Spanish).issn("112211111").authorId(List.of(authorEntities[1].getAuthorId())).bookType(List.of(typeEntities[1].getName())).publisher("test2").barcode("abc1").build(),
                BookEntity.builder().bookID("3").name("test2").entryTime(LocalDateTime.now()).description("this is a test book2").status(BookStatus.OCCUPIED).imgUrl("https://localhost/images/book/default.jpg").imgFileName("default.jpg").deposit(new BigDecimal("55")).language(Language.English).issn("11199999001").authorId(List.of(authorEntities[2].getAuthorId(),authorEntities[3].getAuthorId())).bookType(List.of(typeEntities[2].getName(),typeEntities[3].getName())).publisher("test3").barcode("abc12").build(),
                BookEntity.builder().bookID("4").name("test3").entryTime(LocalDateTime.now()).description("this is a test book3").status(BookStatus.OCCUPIED).imgUrl("https://localhost/images/book/default.jpg").imgFileName("default.jpg").deposit(new BigDecimal("99")).language(Language.English).isbn("2222222889").authorId(List.of(authorEntities[2].getAuthorId(),authorEntities[3].getAuthorId())).bookType(List.of(typeEntities[3].getName(),typeEntities[4].getName())).publisher("test4").barcode("abc123").build(),
                BookEntity.builder().bookID("5").name("test4").entryTime(LocalDateTime.now()).description("this is a test book4").status(BookStatus.OCCUPIED).imgUrl("https://localhost/images/book/default.jpg").imgFileName("default.jpg").deposit(new BigDecimal("64")).language(Language.English).isbn("98989777979").authorId(List.of(authorEntities[0].getAuthorId(),authorEntities[2].getAuthorId())).bookType(List.of(typeEntities[4].getName())).publisher("test5").barcode("abc1234").build(),
                BookEntity.builder().bookID("6").name("test5").entryTime(LocalDateTime.now()).description("this is a test book5").status(BookStatus.ENABLE).imgUrl("https://localhost/images/book/default.jpg").imgFileName("default.jpg").deposit(new BigDecimal("64")).language(Language.English).isbn("2222003034002").authorId(List.of(authorEntities[0].getAuthorId(),authorEntities[2].getAuthorId())).bookType(List.of(typeEntities[4].getName())).publisher("test6").barcode("abc123456").build(),
                BookEntity.builder().bookID("7").name("test6").entryTime(LocalDateTime.now()).description("this is a test book6").status(BookStatus.ENABLE).imgUrl("https://localhost/images/book/default.jpg").imgFileName("default.jpg").deposit(new BigDecimal("64")).language(Language.English).isbn("22222222222").authorId(List.of(authorEntities[0].getAuthorId(),authorEntities[2].getAuthorId())).bookType(List.of(typeEntities[4].getName())).publisher("test7").barcode("abc1234567").build(),
                BookEntity.builder().bookID("8").name("lending").entryTime(LocalDateTime.now()).description("lending").status(BookStatus.OCCUPIED).imgUrl("https://localhost/images/book/default.jpg").imgFileName("default.jpg").deposit(new BigDecimal("59")).language(Language.English).isbn("sdadadad").authorId(List.of()).bookType(List.of()).publisher("lending").barcode("lending").build(),
                BookEntity.builder().bookID("9").name("lending").entryTime(LocalDateTime.now()).description("lending").status(BookStatus.ENABLE).imgUrl("https://localhost/images/book/default.jpg").imgFileName("default.jpg").deposit(new BigDecimal("78")).language(Language.English).isbn("sdadad78j").authorId(List.of()).bookType(List.of()).publisher("lending").barcode("lending").build(),
                BookEntity.builder().bookID("10").name("lending").entryTime(LocalDateTime.now()).description("lending").status(BookStatus.ENABLE).imgUrl("https://localhost/images/book/default.jpg").imgFileName("default.jpg").deposit(new BigDecimal("78")).language(Language.English).isbn("sdadad78j").authorId(List.of()).bookType(List.of()).publisher("lending").barcode("lending").build(),
        };
        this.bookDao.saveAll(Arrays.asList(bookEntities));

        LogManager.getLogger(this.getClass()).warn("-------      Initial LendingData      -----------");
        ReturnSendEmail returnSendEmail = new ReturnSendEmail().init();
        LendingDataEntity[] lendingDataEntities = {
                LendingDataEntity.builder().reference("1").book(bookEntities[7]).user(userEntities[7]).lendingTime(LocalDateTime.now()).limitTime(LocalDateTime.now().plusHours(1)).status(false).returnSendEmail(returnSendEmail).build(),
                LendingDataEntity.builder().reference("2").book(bookEntities[9]).user(userEntities[7]).lendingTime(LocalDateTime.now()).limitTime(LocalDateTime.now().plusHours(1)).status(false).returnSendEmail(returnSendEmail).build(),
                LendingDataEntity.builder().reference("3").book(bookEntities[4]).user(userEntities[7]).lendingTime(LocalDateTime.now()).limitTime(LocalDateTime.now().plusHours(1)).status(true).returnSendEmail(returnSendEmail).build(),
                LendingDataEntity.builder().reference("4").book(bookEntities[8]).user(userEntities[7]).lendingTime(LocalDateTime.now()).limitTime(LocalDateTime.now().plusHours(1)).status(false).returnSendEmail(returnSendEmail).build(),
                LendingDataEntity.builder().reference("5").book(bookEntities[8]).user(userEntities[7]).lendingTime(LocalDateTime.now()).limitTime(LocalDateTime.now().plusHours(1)).status(true).returnSendEmail(returnSendEmail).build(),
                LendingDataEntity.builder().reference("6").book(bookEntities[4]).user(userEntities[7]).lendingTime(LocalDateTime.now().minusDays(10)).limitTime(LocalDateTime.now().plusHours(1)).status(false).returnSendEmail(returnSendEmail).build(),
                LendingDataEntity.builder().reference("7").book(bookEntities[4]).user(userEntities[8]).lendingTime(LocalDateTime.now().minusDays(33)).limitTime(LocalDateTime.now().minusDays(100)).status(false).returnSendEmail(returnSendEmail).build(),
                LendingDataEntity.builder().reference("8").book(bookEntities[4]).user(userEntities[8]).lendingTime(LocalDateTime.now().minusDays(400)).limitTime(LocalDateTime.now().plusDays(1)).status(false).returnSendEmail(returnSendEmail).build(),
        };
        this.lendingDataDao.saveAll(Arrays.asList(lendingDataEntities));

        LogManager.getLogger(this.getClass()).warn("-------      Initial ReturnData      -----------");
        ReturnDataEntity[] returnDataEntities = {
                ReturnDataEntity.builder().reference("2").limitTime(lendingDataEntities[1].getLimitTime()).lendingTime(lendingDataEntities[1].getLendingTime()).user(lendingDataEntities[1].getUser()).book(lendingDataEntities[1].getBook()).returnTime(LocalDateTime.now()).returnStatus(ReturnStatus.NO_RETURN).build(),
                ReturnDataEntity.builder().reference("3").limitTime(lendingDataEntities[2].getLimitTime()).lendingTime(lendingDataEntities[2].getLendingTime()).user(lendingDataEntities[2].getUser()).book(lendingDataEntities[2].getBook()).returnTime(LocalDateTime.now()).returnStatus(ReturnStatus.WAITING_FOR_VERIFICATION).build(),
                ReturnDataEntity.builder().reference("4").limitTime(lendingDataEntities[3].getLimitTime()).lendingTime(lendingDataEntities[3].getLendingTime()).user(lendingDataEntities[3].getUser()).book(lendingDataEntities[3].getBook()).returnTime(LocalDateTime.now().minusDays(2)).returnStatus(ReturnStatus.NO_RETURN).build(),
                ReturnDataEntity.builder().reference("5").limitTime(lendingDataEntities[3].getLimitTime()).lendingTime(lendingDataEntities[3].getLendingTime()).user(lendingDataEntities[3].getUser()).book(lendingDataEntities[3].getBook()).returnTime(LocalDateTime.now().plusDays(90)).returnStatus(ReturnStatus.WAITING_FOR_VERIFICATION).build(),
        };
        this.returnDataDao.saveAll(Arrays.asList(returnDataEntities));

        LogManager.getLogger(this.getClass()).warn("-------      Initial CollectionList      -----------");
        CollectionListEntity[] collectionListEntities = {
                CollectionListEntity.builder().bookId(List.of(bookEntities[1].getBookID(),bookEntities[2].getBookID())).telephone("+34666666666").build(),
                CollectionListEntity.builder().bookId(List.of(bookEntities[2].getBookID(),bookEntities[2].getBookID())).telephone("+34666000001").build(),
                CollectionListEntity.builder().bookId(List.of(bookEntities[3].getBookID(),bookEntities[2].getBookID())).telephone("+34666000002").build(),
                CollectionListEntity.builder().bookId(List.of(bookEntities[4].getBookID(),bookEntities[2].getBookID())).telephone("+34666"      ).build(),
                CollectionListEntity.builder().bookId(List.of(bookEntities[5].getBookID(),bookEntities[2].getBookID())).telephone("+34645321068").build(),
                CollectionListEntity.builder().bookId(List.of(bookEntities[6].getBookID(),bookEntities[2].getBookID())).telephone("+34123"      ).build(),
                CollectionListEntity.builder().bookId(List.of(bookEntities[7].getBookID(),bookEntities[2].getBookID())).telephone("+34321"      ).build(),
                CollectionListEntity.builder().bookId(List.of()).telephone("+34990099009").build(),
                CollectionListEntity.builder().bookId(List.of()).telephone("+34333333333").build(),
        };
        this.collectionListDao.saveAll(Arrays.asList(collectionListEntities));
    }

}
