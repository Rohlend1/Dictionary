package com.example.dictionary.services;

import com.example.dictionary.dto.WordCardDTO;
import com.example.dictionary.entities.WordCard;
import com.example.dictionary.repositories.WordCardRepository;
import com.example.dictionary.util.CardStatus;
import com.example.dictionary.util.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WordCardService {

    private final WordCardRepository wordCardRepository;
    private final Converter converter;

    @Transactional(readOnly = true)
    public List<WordCardDTO> findAll(){
        return converter.convertToWordCardDtoList(wordCardRepository.findAll());
    }

    public void vote(Long id, Boolean isAgainst){
        WordCard wordCard = wordCardRepository.findById(id).orElseThrow(RuntimeException::new);
        if (isAgainst) {
            wordCard.setVotesAgainst(wordCard.getVotesAgainst().add(BigInteger.ONE));
        } else {
            wordCard.setVotesFor(wordCard.getVotesAgainst().add(BigInteger.ONE));
        }
    }

    @Transactional(readOnly = true)
    public List<WordCard> findAllByDecisionTimeBefore(LocalDateTime dateTime){
        return wordCardRepository.findAllByDecisionTimeBefore(dateTime);
    }

    public void deleteAll(List<WordCard> wordCards){
        wordCardRepository.deleteAll(wordCards);
    }

    @Transactional(readOnly = true)
    public List<WordCardDTO> findAllByStatus(CardStatus status){
        return converter.convertToWordCardDtoList(wordCardRepository.findAllByStatus(status));
    }
}
