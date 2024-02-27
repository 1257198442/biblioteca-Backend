package com.example.demo.domain.exceptions;

public class AuthenticationException extends RuntimeException{
    private static final String DESCRIPTION = "Authentication exception";
    public AuthenticationException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }
}
