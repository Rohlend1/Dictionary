package com.example.dictionary.services;

import com.example.dictionary.entities.Word;
import com.example.dictionary.repositories.WordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class WordService {
    private final WordRepository wordRepository;

    public WordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public List<Word> getAllWords(){
        return wordRepository.findAll();
    }
    public Word findWordByValue(String value){
        return wordRepository.findByValueEquals(value);
    }
    public Word findWordByTranslate(String translate){
        return wordRepository.findByTranslateEquals(translate);
    }
}
