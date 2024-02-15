package com.example.demo.domain.models;

import com.example.demo.domain.exceptions.BadRequestException;

public enum Admin {
    ROOT, ADMINISTRATOR, CLIENT, AUTHENTICATED, BAN;
    public static final String PREFIX = "ROLE_";

    public String withPrefix() {
        return PREFIX + this.toString();
    }

}
