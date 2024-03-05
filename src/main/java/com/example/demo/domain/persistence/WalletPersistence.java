package com.example.demo.domain.persistence;

import com.example.demo.domain.models.Wallet;

public interface WalletPersistence {
    Wallet read(String telephone);
}