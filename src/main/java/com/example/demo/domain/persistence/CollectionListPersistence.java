package com.example.demo.domain.persistence;

import com.example.demo.domain.models.CollectionList;

public interface CollectionListPersistence {
    CollectionList read(String telephone);
    CollectionList update(CollectionList collectionList);
}
