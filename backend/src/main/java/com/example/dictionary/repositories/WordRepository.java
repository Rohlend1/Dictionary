package com.example.dictionary.repositories;

import com.example.dictionary.entities.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    List<Word> findAllByValueEquals(String value);
    Word findByTranslateEquals(String translate);

    List<Word> findAllByValue(String word);
}
