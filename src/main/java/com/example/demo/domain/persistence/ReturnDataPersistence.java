package com.example.demo.domain.persistence;

import com.example.demo.domain.models.ReturnData;

import java.util.List;

public interface ReturnDataPersistence {
    ReturnData create(ReturnData restitution);
    ReturnData read(String reference);
    List<ReturnData> readAll();
}