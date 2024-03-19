package com.example.demo.domain.service;

import com.example.demo.TestConfig;
import com.example.demo.domain.exceptions.ConflictException;
import com.example.demo.domain.exceptions.NotFoundException;
import com.example.demo.domain.models.Type;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@TestConfig
public class TypeServiceTests {
    @Autowired
    private TypeService typeService;

    @Test
    void testCreateAndRead(){
        Type type = Type.builder().name("testCreate").description("test text").build();
        typeService.create(type);
        assertThrows(ConflictException.class,()->typeService.create(type));
        Type type1 = typeService.read("testCreate");
        assertEquals(type.getDescription(),type1.getDescription());
        assertEquals(type.getName(),type1.getName());
        assertThrows(NotFoundException.class,()->typeService.read("null"));
    }
    @Test
    void testReadAll(){
        assertNotNull(typeService.readAll());
    }
    @Test
    void testGetType(){
        Type type = typeService.getType("test");
        assertEquals("1",type.getDescription());
        assertNull(typeService.getType("null"));
    }
}
