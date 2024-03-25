package com.example.demo.domain.persistence;

import com.example.demo.domain.models.ReturnData;

import java.util.List;

public interface ReturnDataPersistence {
    ReturnData create(ReturnData restitution);
    ReturnData read(String reference);
    List<ReturnData> readAll();
    ReturnData update(ReturnData restitution);
    List<ReturnData> readByUserTelephone(String telephone);
    List<ReturnData> readByBookId(String bookId);
    Boolean existReference(String reference);
}