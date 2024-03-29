package com.example.dictionary.services;

import com.example.dictionary.entities.WordCard;
import com.example.dictionary.repositories.WordCardRepository;
import com.example.dictionary.repositories.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.dictionary.util.CardStatus.MODERATED;

@Service
@Transactional
@RequiredArgsConstructor
public class ModerationService {

    private final WordCardRepository wordCardRepository;
    private final WordRepository wordRepository;

    public void changeWordCardStatus(String word, String translate){
        WordCard wordCard = wordCardRepository.findAllByWordAndTranslate(word, translate);
        wordCard.setStatus(MODERATED);
    }

    public void deleteWordById(Long id){
        wordRepository.deleteById(id);
    }
}
