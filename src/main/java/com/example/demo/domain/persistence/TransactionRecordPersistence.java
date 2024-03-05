package com.example.demo.domain.persistence;

import com.example.demo.domain.models.TransactionRecord;

import java.util.List;

public interface TransactionRecordPersistence {
    TransactionRecord create(TransactionRecord transactionRecord);
}
