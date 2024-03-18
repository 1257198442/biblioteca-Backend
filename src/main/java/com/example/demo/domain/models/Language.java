package com.example.demo.domain.models;

import com.example.demo.domain.exceptions.BadRequestException;
import com.example.demo.domain.exceptions.UnprocessableEntityException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Language {
    Chinese,English,Spanish,French,German,Latin,Japanese,Arabic,Hindi,Portuguese,Korean,Ukrainian,Italian,Norwegian,Dutch,Polish;

    public static Language fromString(String language) {
        try {
            return Language.valueOf(language);
        } catch (IllegalArgumentException e) {
            throw new UnprocessableEntityException("The language is error");
        }
    }
    public static List<String> getAllLanguages() {
        return Arrays.stream(Language.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
