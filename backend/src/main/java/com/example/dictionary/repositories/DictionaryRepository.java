package com.example.dictionary.repositories;

import com.example.dictionary.entities.Dictionary;
import org.hibernate.annotations.processing.HQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryRepository extends MongoRepository<Dictionary, Long> {
    Dictionary findDictionariesByOwner(Long personId);
}
