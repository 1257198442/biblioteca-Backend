package com.example.demo.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseSeederService {
    private final SeederServe seederServe;
    @Autowired
    public DatabaseSeederService(SeederServe seederServe) {
        this.seederServe = seederServe;
        this.seedDatabase();
    }

    public void seedDatabase() {
        this.seederServe.seedDatabase();
    }

    public void deleteAll() {
        this.seederServe.deleteAll();
    }

    public void reSeedDatabase() {
        this.deleteAll();
        this.seedDatabase();
    }
}
