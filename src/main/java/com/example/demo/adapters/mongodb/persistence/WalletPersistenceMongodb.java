package com.example.demo.adapters.mongodb.persistence;

import com.example.demo.adapters.mongodb.daos.WalletRepository;
import com.example.demo.domain.exceptions.NotFoundException;
import com.example.demo.domain.models.Wallet;
import com.example.demo.domain.persistence.WalletPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class WalletPersistenceMongodb implements WalletPersistence {
    private final WalletRepository walletDao;
    @Autowired
    public WalletPersistenceMongodb(WalletRepository walletDao){
        this.walletDao = walletDao;
    }
    @Override
    public Wallet read(String telephone) {
        return this.walletDao.findByTelephone(telephone)
                .orElseThrow(()->new NotFoundException("Wallet: "+telephone+" is not Fount"))
                .toWallet();
    }
}
