package com.example.dictionary.services;

import com.example.dictionary.dto.WordDTO;
import com.example.dictionary.entities.Dictionary;
import com.example.dictionary.entities.Word;
import com.example.dictionary.repositories.WordRepository;
import com.example.dictionary.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class WordService {
    private final WordRepository wordRepository;
    private final Converter converter;

    @Autowired
    public WordService(WordRepository wordRepository, Converter converter) {
        this.wordRepository = wordRepository;
        this.converter = converter;
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

    public List<WordDTO> findByValueStartsWith(String startsWith, Dictionary dictionary){
        List<Word> words = dictionary.getWords();
        return words.stream().filter(w -> w.getValue().startsWith(startsWith)).map(converter::convertToWordDTO).toList();
    }
}
