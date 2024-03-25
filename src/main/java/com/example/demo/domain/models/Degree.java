package com.example.demo.domain.models;

import com.example.demo.domain.exceptions.UnprocessableEntityException;

import java.math.BigDecimal;

public enum Degree {
    PERFECT, SLIGHTLY_DAMAGED,MISSING_PAGES,BADLY_DAMAGED,IRREPARABLE;
    public static Degree fromString(String admin) {
        try {
            return Degree.valueOf(admin);
        } catch (IllegalArgumentException e) {
            throw new UnprocessableEntityException("The degree is invalid");
        }
    }

    public static BigDecimal percentageAppearance(Degree degree) {
        switch (degree) {
            case SLIGHTLY_DAMAGED:
                return BigDecimal.valueOf(0.1);
            case MISSING_PAGES:
                return BigDecimal.valueOf(0.3);
            case BADLY_DAMAGED:
                return BigDecimal.valueOf(0.8);
            case IRREPARABLE:
                return BigDecimal.valueOf(1);
            default:
                return BigDecimal.valueOf(0);
        }
    }

}
