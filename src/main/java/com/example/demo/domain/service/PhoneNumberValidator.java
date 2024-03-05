package com.example.demo.domain.service;

import com.example.demo.domain.exceptions.UnprocessableEntityException;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PhoneNumberValidator {
    private String regex = "\\+(9[976]\\d|8[987530]\\d|6[987]\\d|5[90]\\d|42\\d|3[875]\\d|" +
            "2[98654321]\\d|9[8543210]|8[6421]|6[6543210]|5[87654321]|" +
            "4[987654310]|3[9643210]|2[70]|7|1)\\d{1,14}$";

    public String validate(String phoneNumber) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);

        if (matcher.matches()) {
            return phoneNumber;
        } else {
            throw new UnprocessableEntityException("The phone number format is invalid. Expected format:"+phoneNumber);
        }
  }
}