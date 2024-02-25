package com.example.dictionary.services;

import com.example.dictionary.entities.Word;
import com.example.dictionary.entities.WordCard;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.dictionary.util.CardStatus.MODERATED;
import static com.example.dictionary.util.Constants.MAIN_PAGE_CACHE_KEY;

@Service
@RequiredArgsConstructor
public class WordCardCheckTimeService {

    private final WordCardService wordCardService;
    private final WordService wordService;
    private final RedisService redisService;

    //ToDo переделать, добавить транзакции
    @Async
    @Scheduled(fixedRate = 60000)
    public void acceptOrDeclineWordCard() {
        List<WordCard> wordCards = wordCardService.findAllByDecisionTimeBefore(LocalDateTime.now())
                .stream().filter(wordCard ->
                        0 < wordCard.getVotesFor().compareTo(wordCard.getVotesAgainst())
                                && wordCard.getStatus().equals(MODERATED))
                .toList();

        List<Word> words = new ArrayList<>(wordCards
                .stream()
                .map(wordCard -> {
                    Word word = new Word();
                    word.setTranslate(wordCard.getTranslate());
                    word.setValue(wordCard.getWord());
                    return word;
                }).toList());
        wordCardService.deleteAll(wordCards);
        List<Word> redisWords = redisService.get(MAIN_PAGE_CACHE_KEY);
        if(redisWords == null){
            redisService.add(MAIN_PAGE_CACHE_KEY, words);
        }
        else{
            words.addAll(0, redisWords);
            redisService.add(MAIN_PAGE_CACHE_KEY, words);
        }
        wordService.saveAll(words);
    }
}
