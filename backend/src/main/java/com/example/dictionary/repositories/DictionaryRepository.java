package com.example.dictionary.repositories;

import com.example.dictionary.entities.Dictionary;
import com.example.dictionary.entities.Word;
import com.example.dictionary.projections.WordsProjection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DictionaryRepository extends MongoRepository<Dictionary, String> {
    Dictionary findDictionariesByOwner(Long ownerId);

    List<Dictionary> findAllByOwner(Long ownerId);

    @Query(value = "{'_id': ?0}", fields = "{'words' : 1}")
    Optional<WordsProjection> findWordsById(String dictId);

    @Query(value = "{'owner': ?0}", fields = "{'_id' : 1}")
    List<String> findAllDictIds(Long ownerId);

}
