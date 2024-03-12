package com.example.dictionary.repositories;

import com.example.dictionary.entities.WordCard;
import com.example.dictionary.util.CardStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WordCardRepository extends JpaRepository<WordCard, Long> {

    List<WordCard> findAllByDecisionTimeBefore(LocalDateTime dateTime);

    List<WordCard> findAllByStatus(CardStatus status);

    WordCard findAllByWordAndTranslate(String word, String translate);

    List<WordCard> findAllByWord(String word);
}
