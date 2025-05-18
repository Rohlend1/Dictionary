package com.example.dictionary.services;

import com.example.dictionary.dto.WordCardDTO;
import com.example.dictionary.entities.WordCard;
import com.example.dictionary.repositories.WordCardRepository;
import com.example.dictionary.repositories.WordRepository;
import com.example.dictionary.util.CardStatus;
import com.example.dictionary.util.Converter;
import com.example.dictionary.util.errors.NotUniqueEntity;
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
    private final WordRepository wordRepository;
    private final Converter converter;

    @Transactional(readOnly = true)
    public List<WordCardDTO> findAll(){
        return converter.convertToWordCardDtoList(wordCardRepository.findAll());
    }

    public void vote(Long id, Long userId, Boolean isAgainst){
        WordCard wordCard = wordCardRepository.findById(id).orElseThrow(RuntimeException::new);
        if (isAgainst) {
            wordCard.setVotesAgainst(getVotesSafety(wordCard.getVotesAgainst()).add(BigInteger.ONE));
        } else {
            wordCard.setVotesFor(getVotesSafety(wordCard.getVotesFor()).add(BigInteger.ONE));
        }
        wordCard.getVoters().add(userId);
    }

    @Transactional(readOnly = true)
    public List<WordCard> findAllForModeration(LocalDateTime dateTime){
        return wordCardRepository.findAllByDecisionTimeBeforeAndStatus(dateTime, CardStatus.MODERATED);
    }

    public void deleteAll(List<WordCard> wordCards){
        wordCardRepository.deleteAll(wordCards);
    }

    @Transactional(readOnly = true)
    public List<WordCardDTO> findAllByStatus(CardStatus status){
        return converter.convertToWordCardDtoList(wordCardRepository.findAllByStatus(status));
    }

    public void save(WordCardDTO wordCardDTO){
        if(!wordCardRepository.findAllByWord(wordCardDTO.getWord()).isEmpty()
                || !wordRepository.findAllByValue(wordCardDTO.getWord()).isEmpty()) {
            throw new NotUniqueEntity("NOT UNIQUE WORD CARD");
        }
        WordCard wordCard = converter.convertToWordCard(wordCardDTO);
        wordCard.setStatus(CardStatus.CREATED);
        wordCard.setDecisionTime(LocalDateTime.now().plusDays(7));
        wordCardRepository.save(wordCard);
    }

    private BigInteger getVotesSafety(BigInteger votes){
        return votes == null ? BigInteger.ZERO : votes;
    }
}
