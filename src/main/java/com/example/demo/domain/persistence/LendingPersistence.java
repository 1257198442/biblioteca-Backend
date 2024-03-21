package com.example.demo.domain.persistence;

import com.example.demo.domain.models.Lending;

import java.util.List;

public interface LendingPersistence {
    Lending create(Lending lending);
    Lending read(String reference);
    List<Lending> readByUserTelephone(String telephone);
    List<Lending> readAll();

}
