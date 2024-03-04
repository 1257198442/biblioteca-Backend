package com.example.demo.domain.service;

import com.example.demo.domain.models.Wallet;
import com.example.demo.domain.persistence.WalletPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletService {
    private final WalletPersistence walletPersistence;
    @Autowired
    public WalletService(WalletPersistence walletPersistence){
        this.walletPersistence = walletPersistence;
    }
    public Wallet readByTelephone(String telephone){
        return this.walletPersistence.read(telephone);
    }
}
