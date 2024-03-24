package com.example.demo.domain.service;

import com.example.demo.TestConfig;
import com.example.demo.adapters.rest.dto.BookDamageDegreeDto;
import com.example.demo.domain.models.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
public class ReturnDataServiceTests {
    @Autowired
    private ReturnDataService returnDataService;
    @Autowired
    private LendingDataService lendingService;
    @Autowired
    private BookService bookService;
    @Test
    void testCreateAndRead(){
        String reference = returnDataService.create("4").getReference();
        LendingData lending = lendingService.read("4");
        ReturnData restitution = returnDataService.read(reference);
        assertEquals(lending.getLendingTime(),restitution.getLendingTime());
        assertEquals(lending.getBook(),restitution.getBook());
        assertEquals(lending.getUser(),restitution.getUser());
    }
    @Test
    void testReadAll(){
        assertNotNull(returnDataService.readAll());
    }

    @Test
    void testBookIsReturnAndNoReturn(){
        ReturnData returnData =returnDataService.read("3");
        assertEquals(ReturnStatus.WAITING_FOR_VERIFICATION,returnData.getReturnStatus());
        returnDataService.bookIsNoReturn("3");
        returnData =returnDataService.read("3");
        assertEquals(ReturnStatus.NO_RETURN,returnData.getReturnStatus());
        assertEquals(bookService.read(returnData.getBook().getBookID()).getStatus(), BookStatus.OCCUPIED);

        BookDamageDegreeDto bookDamageDegreeDto = BookDamageDegreeDto.builder().degree("PERFECT").addendum("bien").build();
        returnDataService.bookIsReturn("3",bookDamageDegreeDto);
        returnData =returnDataService.read("3");
        assertEquals(ReturnStatus.IS_RETURN,returnData.getReturnStatus());
        assertEquals(bookService.read(returnData.getBook().getBookID()).getStatus(), BookStatus.ENABLE);
        assertEquals(Degree.PERFECT,returnData.getBookDamageDegree().getDegree());
        assertEquals("bien",returnData.getBookDamageDegree().getAddendum());
    }

    @Test
    void testGetDays(){
        ReturnData returnData = ReturnData.builder()
                .limitTime(LocalDateTime.now())
                .returnTime(LocalDateTime.now().minusDays(1)).build();
        assertEquals(1,returnDataService.getDays(returnData));
        returnData.setReturnTime(LocalDateTime.now().minusDays(3));
        assertEquals(3,returnDataService.getDays(returnData));
    }
    @Test
    void testGetPercentagePostponement(){
        assertEquals(BigDecimal.valueOf(0.05),returnDataService.getPercentagePostponement(1));
        assertEquals(BigDecimal.valueOf(0.8),returnDataService.getPercentagePostponement(17));
        assertEquals(BigDecimal.valueOf(1.0),returnDataService.getPercentagePostponement(33));
    }
}
