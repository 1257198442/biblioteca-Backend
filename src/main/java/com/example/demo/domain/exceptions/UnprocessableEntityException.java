package com.example.demo.domain.exceptions;

public class UnprocessableEntityException extends RuntimeException {
    private static final String DESCRIPTION = "Unprocessable Entity";

    public UnprocessableEntityException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }
}