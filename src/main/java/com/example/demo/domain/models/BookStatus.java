package com.example.demo.domain.models;

import com.example.demo.domain.exceptions.BadRequestException;

public enum BookStatus {
    ENABLE,OCCUPIED,DISABLE;

    public static BookStatus fromString(String status) {
        try {
            return BookStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("The status "+status+" is invalid");
        }
    }
}