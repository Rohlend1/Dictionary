package com.example.dictionary.services;

import com.example.dictionary.entities.Dictionary;
import com.example.dictionary.entities.Word;
import com.example.dictionary.repositories.DictionaryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class DictionaryService {

    private final DictionaryRepository dictionaryRepository;
    private final WordService wordService;

    public DictionaryService(DictionaryRepository dictionaryRepository, WordService wordService) {
        this.dictionaryRepository = dictionaryRepository;
        this.wordService = wordService;
    }

    public List<Dictionary> getAllDictionaries(){
        return dictionaryRepository.findAll();
    }

    public Dictionary getDictionaryById(int dictionaryId){
        return dictionaryRepository.findById(dictionaryId).orElse(null);
    }
    public List<Word> getAllWordsByDictionaryId(int dictionaryId){
        Dictionary dictionary = getDictionaryById(dictionaryId);
        return dictionary.getWords();
    }

}
