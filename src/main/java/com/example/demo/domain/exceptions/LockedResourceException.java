package com.example.demo.domain.exceptions;

public class LockedResourceException extends RuntimeException {
    private static final String DESCRIPTION = "Locked resource exception";

    public LockedResourceException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }
}