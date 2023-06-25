package com.example.dictionary.services;

import com.example.dictionary.dto.WordDTO;
import com.example.dictionary.entities.Word;
import com.example.dictionary.repositories.WordRepository;
import com.example.dictionary.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

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

    public List<Word> findAll(){
        return wordRepository.findAll();
    }

    public List<Word> findAll(int page, int itemsPerPage){
        return wordRepository.findAll(PageRequest.of(page,itemsPerPage)).getContent();
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.isBlank();
    }

    private List<WordDTO> findByStartsWith(String startsWith, List<Word> words, Function<Word, String> getField) {
        if (isNullOrEmpty(startsWith)) {
            return words.stream().map(converter::convertToWordDTO).toList();
        }
        return words.stream()
                .filter(w -> getField.apply(w).startsWith(startsWith))
                .map(converter::convertToWordDTO)
                .toList();
    }

    public List<WordDTO> findByTranslate(String startsWith, List<Word> words) {
        return findByStartsWith(startsWith, words, Word::getTranslate);
    }

    public List<WordDTO> findByValue(String startsWith, List<Word> words) {
        return findByStartsWith(startsWith, words, Word::getValue);
    }
}
