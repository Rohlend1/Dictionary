package com.example.dictionary.repositories;

import com.example.dictionary.entities.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryRepository extends JpaRepository<Dictionary, Long> {
    @Query("SELECT d FROM Dictionary as d WHERE d.owner.id = :personId")
    Dictionary findDictionariesByOwner(Long personId);
}
