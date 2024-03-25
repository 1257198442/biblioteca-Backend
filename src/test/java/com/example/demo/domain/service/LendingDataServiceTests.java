package com.example.demo.domain.service;

import com.example.demo.TestConfig;
import com.example.demo.adapters.rest.dto.LendingDataUploadDto;
import com.example.demo.domain.exceptions.ConflictException;
import com.example.demo.domain.exceptions.NotFoundException;
import com.example.demo.domain.models.LendingData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
public class LendingDataServiceTests {
    @Autowired
    private LendingDataService lendingDataService;

    @Test
    void testCreateAndReadAndReadByTelephone(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LendingDataUploadDto lendingDto = LendingDataUploadDto.builder()
                .bookId("9")
                .limitTime(LocalDateTime.now().plusMonths(2).format(formatter))
                .password("6")
                .telephone("+34990099009").build();
        String reference = lendingDataService.create(lendingDto).getReference();
        LendingData lendingData = lendingDataService.read(reference);
        assertEquals(lendingDto.getBookId(), lendingData.getBook().getBookID());
        assertEquals(lendingDto.getTelephone(), lendingData.getUser().getTelephone());
        assertEquals(lendingDto.getLimitTime(), lendingData.getLimitTime().format(formatter));
        assertNotNull(lendingData.getLendingTime());
        //book is lending
        assertThrows(ConflictException.class,()-> lendingDataService.create(lendingDto));
        //telephone not found
        lendingDto.setBookId("null");
        assertThrows(NotFoundException.class,()-> lendingDataService.create(lendingDto));
        assertNotNull(lendingDataService.readAllByUserTelephone("+34990099009"));
    }
    @Test
    void testReadAll(){
        assertNotNull( lendingDataService.readAll());
    }

    @Test
    void testReadAllByTelephone(){
        assertNotNull(lendingDataService.readAllByUserTelephone("+34990099009"));
    }

    @Test
    void testReadNoReturnByTelephone(){
        assertNotNull(lendingDataService.readNoReturnByTelephone("+34990099009"));
    }
    @Test
    void testReadUserByExtensionBeyond30Days(){

    }

}
