package com.example.demo.adapters.mongodb.persistence;

import com.example.demo.adapters.mongodb.daos.TransactionRecordDao;
import com.example.demo.adapters.mongodb.daos.WalletRepository;
import com.example.demo.adapters.mongodb.entities.TransactionRecordEntity;
import com.example.demo.adapters.mongodb.entities.WalletEntity;
import com.example.demo.domain.exceptions.ForbiddenException;
import com.example.demo.domain.exceptions.NotFoundException;
import com.example.demo.domain.models.TransactionRecord;
import com.example.demo.domain.persistence.TransactionRecordPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public class TransactionRecordPersistenceMongodb implements TransactionRecordPersistence {
    private final TransactionRecordDao transactionRecordDao;
    private final WalletRepository walletDao;
    @Autowired
    public TransactionRecordPersistenceMongodb(TransactionRecordDao transactionRecordDao, WalletRepository walletDao){
        this.transactionRecordDao = transactionRecordDao;
        this.walletDao = walletDao;
    }

    @Override
    public TransactionRecord create(TransactionRecord transactionRecord) {
        WalletEntity wallet = this.walletDao.findByTelephone(transactionRecord.telephone)
                .orElseThrow(()->new NotFoundException("wallet: "+transactionRecord.telephone+" is not Found"));
        BigDecimal money = wallet.getBalance().add(transactionRecord.amount);
        if(money.compareTo(BigDecimal.ZERO)>=0){
            wallet.setBalance(money);
            this.walletDao.save(wallet);
            return this.transactionRecordDao
                    .save(new TransactionRecordEntity(transactionRecord))
                    .toTransactionRecord();
        }else {
            throw new ForbiddenException("The balance is insufficient.");
        }
    }
}
