package com.example.demo.domain.service;

import com.example.demo.TestConfig;
import com.example.demo.domain.models.BookStatus;
import com.example.demo.domain.models.LendingData;
import com.example.demo.domain.models.ReturnData;
import com.example.demo.domain.models.ReturnStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
        returnDataService.bookIsNoReturn("3");
        ReturnData returnData =returnDataService.read("3");
        assertEquals(ReturnStatus.NO_RETURN,returnData.getReturnStatus());
        assertEquals(bookService.read(returnData.getBook().getBookID()).getStatus(), BookStatus.OCCUPIED);
        returnDataService.bookIsReturn("3");
        ReturnData returnData1 =returnDataService.read("3");
        assertEquals(ReturnStatus.IS_RETURN,returnData1.getReturnStatus());
        assertEquals(bookService.read(returnData.getBook().getBookID()).getStatus(), BookStatus.ENABLE);

    }
}
