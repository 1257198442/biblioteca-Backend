package com.example.demo.domain.exceptions;

public class MaxUploadSizeExceededException extends RuntimeException {
    private static final String DESCRIPTION = "Max Upload Size Exceeded Exception (413)";

    public MaxUploadSizeExceededException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }

}