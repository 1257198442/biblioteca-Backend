package com.example.demo.domain.models;

public enum Role {
    ROOT, ADMINISTRATOR, CLIENT, AUTHENTICATED, BAN;
    public static final String PREFIX = "ROLE_";

    public String withPrefix() {
        return PREFIX + this.toString();
    }

}
