package com.example.dictionary.services;

import com.example.dictionary.repositories.DictionaryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DictionaryService {

    private final DictionaryRepository dictionaryRepository;
    private final WordService wordService;

    public DictionaryService(DictionaryRepository dictionaryRepository, WordService wordService) {
        this.dictionaryRepository = dictionaryRepository;
        this.wordService = wordService;
    }
}
