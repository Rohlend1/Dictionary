package com.example.dictionary.services;

import com.example.dictionary.entities.Word;
import com.example.dictionary.entities.WordCard;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.dictionary.util.Constants.MAIN_PAGE_CACHE_KEY;

@Service
@RequiredArgsConstructor
public class WordCardCheckTimeService {

    private final WordCardService wordCardService;
    private final WordService wordService;
    private final RedisService redisService;

    @Async
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void acceptOrDeclineWordCard() {
        List<WordCard> wordCards = wordCardService.findAllForModeration(LocalDateTime.now())
                .stream().filter(wordCard ->
                        0 < wordCard.getVotesFor().compareTo(wordCard.getVotesAgainst()))
                .toList();

        List<Word> words = new ArrayList<>(wordCards
                .stream()
                .map(wordCard -> {
                    Word word = new Word();
                    word.setTranslate(wordCard.getTranslate());
                    word.setValue(wordCard.getWord());
                    return word;
                }).toList());
        List<Word> redisWords = redisService.get(MAIN_PAGE_CACHE_KEY);
        if(redisWords == null){
            redisService.add(MAIN_PAGE_CACHE_KEY, words);
        }
        else{
            words.addAll(0, redisWords);
            redisService.add(MAIN_PAGE_CACHE_KEY, words);
        }
        wordService.saveAll(words);
        wordCardService.deleteAll(wordCards);
    }
}
