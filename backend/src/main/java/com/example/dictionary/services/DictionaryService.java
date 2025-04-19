package com.example.dictionary.services;

import com.example.dictionary.dto.DictionaryMetaDTO;
import com.example.dictionary.dto.WordDTO;
import com.example.dictionary.entities.Dictionary;
import com.example.dictionary.entities.Word;
import com.example.dictionary.repositories.DictionaryRepository;
import com.example.dictionary.security.JwtUtil;
import com.example.dictionary.util.Converter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Log4j2
public class DictionaryService {

    private final DictionaryRepository dictionaryRepository;
    private final PersonService personService;
    private final Converter converter;
    private final WordService wordService;
    private final JwtUtil jwtUtil;

    @Autowired
    public DictionaryService(DictionaryRepository dictionaryRepository,
                             Converter converter, WordService wordService, PersonService personService, JwtUtil jwtUtil) {
        this.dictionaryRepository = dictionaryRepository;
        this.converter = converter;
        this.wordService = wordService;
        this.personService = personService;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public void deleteDictionary(String dictId, String jwt) {
        Dictionary dict = dictionaryRepository.findById(dictId).orElseThrow();
        long userId = personService.retrieveUserId(jwt);
        if (dict.getOwner() == userId) {
            dictionaryRepository.delete(dict);
        }
        else{
            throw new RuntimeException();
        }
        //TODo заменить exception
    }

    @Transactional
    public void deleteDictionary(String dictId, long userId) {
        Dictionary dict = dictionaryRepository.findById(dictId).orElseThrow();
        if (dict.getOwner() == userId) {
            dictionaryRepository.delete(dict);
        }
        else{
            throw new RuntimeException();
        }
        //TODo заменить exception
    }

    public void deleteDictionaries(Long userId) {
        List<String> dictionariesIds = findAllDictIds(userId);
        for (String dictId : dictionariesIds) {
            deleteDictionary(dictId, userId);
        }

    }

    @Transactional
    public void renameDictionary(String dictId, String newName) {
        Dictionary dictionary = dictionaryRepository.findById(dictId).orElseThrow(); //ToDO добавить exception e.g. DictDoesNotExist
        dictionary.setName(newName);
        dictionaryRepository.save(dictionary);
    }

    @Transactional
    public void addNewWordToDictionary(List<Word> newWords, String dictId) {
        Dictionary dict = dictionaryRepository.findById(dictId).orElseThrow();
        List<Word> modifiableWords = new ArrayList<>(dict.getWords());
        modifiableWords.addAll(findMismatchedWords(newWords, dict.getWords()));
        dict.setWords(modifiableWords);
        dictionaryRepository.save(dict);
    }

    @Transactional
    public void save(DictionaryMetaDTO dictionaryDTO, String jwt) {
        Long ownerId = personService.retrieveUserId(jwt);
        Dictionary dictionary = converter.convertToDictionary(dictionaryDTO);
        dictionary.setOwner(ownerId);
        dictionary.setWords(new ArrayList<>());
        dictionaryRepository.save(dictionary);
    }

    public DictionaryMetaDTO findById(String dictId) {
        Optional<Dictionary> dictionary = dictionaryRepository.findById(dictId);
        dictionary.orElseThrow(RuntimeException::new);
        return converter.convertToDictionaryDTO(dictionary.get());
    }
    //todo Избавиться от всех конвертеров в бизнес слое
    public List<DictionaryMetaDTO> findAll(String jwt) {
        Long ownerId = personService.retrieveUserId(jwt);
        List<Dictionary> dictionaries = dictionaryRepository.findAllByOwner(ownerId);
        return dictionaries.stream().map(converter::convertToDictionaryDTO).toList();
    }

    public Dictionary findSharedDict(String token, Long hashedOwnerId) {
        String dictId = jwtUtil.validateSharedTokenAndRetrieveClaim(token);
        return dictionaryRepository.findById(dictId).orElseThrow();
    }

    public List<String> findAllDictIds(Long userId) {
        return dictionaryRepository.findAllDictIds(userId);
    }

    public List<Word> findAllWordsByDict(String dictId) {
        return dictionaryRepository.findWordsById(dictId).orElseThrow().getWords();
    }

    //Todo убрать что может удалиться не то слово по значению
    // пример: like(например) - like(нравиться)
    // проверить перф этого метода
    @Transactional
    public void deleteWords(String dictId, List<Word> words) {
        Dictionary dict = dictionaryRepository.findById(dictId).orElseThrow();
        List<Word> remainingWords = findMismatchedWords(dict.getWords(), words);
        dict.setWords(remainingWords);
        dictionaryRepository.save(dict);
    }

    public List<WordDTO> findAllWordsExcludedByDictionary(String dictId, int page, int itemsPerPage) {
        List<Word> words = findMismatchedWords(wordService.findAllPagination(page, itemsPerPage), findAllWordsByDict(dictId));
        return words.stream().map(converter::convertToWordDTO).toList();
    }

    private List<Word> findMismatchedWords(List<Word> checkableWords, List<Word> filterWords) {
        Map<String, String> filterValues = new HashMap<>();
        for (Word word : filterWords) {
            filterValues.put(word.getValue(), word.getTranslate());
        }
        return checkableWords.stream()
                .filter(word -> !filterValues.containsKey(word.getValue())).toList();
    }

    public Dictionary getDictionaryDetails(String dictId){
        return dictionaryRepository.findById(dictId).orElseThrow();
    }
}
