package com.example.demo.domain.persistence;

import com.example.demo.domain.models.LendingData;

import java.util.List;

public interface LendingDataPersistence {
    LendingData create(LendingData lending);
    LendingData read(String reference);
    List<LendingData> readByUserTelephone(String telephone);
    List<LendingData> readAll();
    LendingData update(LendingData lending);
}
