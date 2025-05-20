package com.example.dictionary.repositories;

import com.example.dictionary.entities.WordCard;
import com.example.dictionary.util.CardStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WordCardRepository extends JpaRepository<WordCard, Long> {

    List<WordCard> findAllByDecisionTimeBeforeAndStatus(LocalDateTime dateTime, CardStatus status);

    List<WordCard> findAllByStatus(CardStatus status);

    WordCard findAllByWordAndTranslate(String word, String translate);

    List<WordCard> findAllByWord(String word);

    @Modifying
    @Query("update WordCard wc set wc.status = :status where wc.id in :ids")
    void updateWordCardByIds(@Param(value = "ids") List<Long> ids, @Param(value = "status") CardStatus status);
}
