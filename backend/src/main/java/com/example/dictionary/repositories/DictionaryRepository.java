package com.example.dictionary.repositories;

import com.example.dictionary.entities.Dictionary;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface DictionaryRepository extends MongoRepository<Dictionary, String> {
    Dictionary findDictionariesByOwner(Long person);
}
