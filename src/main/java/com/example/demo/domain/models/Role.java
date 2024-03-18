package com.example.demo.domain.models;

import com.example.demo.domain.exceptions.BadRequestException;
import com.example.demo.domain.exceptions.UnprocessableEntityException;

import java.util.List;

public enum Role {
    ROOT, ADMINISTRATOR, CLIENT, AUTHENTICATED, BAN;
    public static final String PREFIX = "ROLE_";

    public String withPrefix() {
        return PREFIX + this.toString();
    }

    public static Role of(String withPrefix) {
        return Role.valueOf(withPrefix.replace(Role.PREFIX, ""));
    }
    public static Role fromString(String role) {
        String normalizedInput = role.toUpperCase();
        try {
            return Role.valueOf(normalizedInput);
        } catch (IllegalArgumentException e) {
            throw new UnprocessableEntityException("The role "+role+" is error");
        }
    }

    public static boolean isCompetent(List<Role> roleList, Role roleClaim){
        for (Role admin:roleList){
            if(admin.equals(roleClaim)){
                return true;
            }
        }
        return false;
    }
}