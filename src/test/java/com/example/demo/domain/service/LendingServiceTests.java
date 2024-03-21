package com.example.demo.domain.service;

import com.example.demo.TestConfig;
import com.example.demo.adapters.rest.dto.LendingUploadDto;
import com.example.demo.domain.exceptions.ConflictException;
import com.example.demo.domain.exceptions.NotFoundException;
import com.example.demo.domain.models.Lending;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
public class LendingServiceTests {
    @Autowired
    private LendingService lendingService;

    @Test
    void testCreateAndReadAndReadByTelephone(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LendingUploadDto lendingDto = LendingUploadDto.builder()
                .bookId("9")
                .limitTime(LocalDateTime.now().plusMonths(2).format(formatter))
                .telephone("+34990099009").build();
        String reference = lendingService.create(lendingDto).getReference();
        Lending lending = lendingService.read(reference);
        assertEquals(lendingDto.getBookId(), lending.getBook().getBookID());
        assertEquals(lendingDto.getTelephone(), lending.getUser().getTelephone());
        assertEquals(lendingDto.getLimitTime(), lending.getLimitTime().format(formatter));
        assertNotNull(lending.getLendingTime());
        //book is lending
        assertThrows(ConflictException.class,()->lendingService.create(lendingDto));
        //telephone not found
        lendingDto.setBookId("null");
        assertThrows(NotFoundException.class,()->lendingService.create(lendingDto));
        assertNotNull(lendingService.readAllByUserTelephone("+34990099009"));
    }
    @Test
    void testReadAll(){
        assertEquals(1,lendingService.readAll().size());
    }
}
