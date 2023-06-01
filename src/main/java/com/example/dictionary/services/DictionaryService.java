package com.example.dictionary.services;

import com.example.dictionary.entities.Dictionary;
import com.example.dictionary.entities.Person;
import com.example.dictionary.entities.Word;
import com.example.dictionary.repositories.DictionaryRepository;
import com.example.dictionary.util.Converter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class DictionaryService {

    private final DictionaryRepository dictionaryRepository;
    private final PersonService personService;
    private final Converter converter;

    public DictionaryService(DictionaryRepository dictionaryRepository, PersonService personService, Converter converter) {
        this.dictionaryRepository = dictionaryRepository;
        this.personService = personService;
        this.converter = converter;
    }

    public List<Dictionary> getAllDictionaries(){
        return dictionaryRepository.findAll();
    }

    public Dictionary getDictionaryById(String dictionaryId){
        return dictionaryRepository.findById(dictionaryId).orElse(null);
    }
    public List<Word> getAllWordsByDictionaryId(String dictionaryId){
        Dictionary dictionary = getDictionaryById(dictionaryId);
        return dictionary.getWords();
    }
    @Transactional
    public void deleteDictionary(Dictionary dictionary){
        dictionaryRepository.delete(dictionary);
    }
    @Transactional
    public void renameDictionary(Dictionary dictionary,String newName){
        dictionary.setName(newName);
        dictionaryRepository.save(dictionary);
    }
    @Transactional
    public void addNewWordToDictionary(List<Word> newWords,Dictionary dictionary){
        List<Word> remainingWords = getMismatchedWords(newWords,dictionary.getWords());
        dictionary.getWords().addAll(remainingWords);
        dictionaryRepository.save(dictionary);
    }
    @Transactional
    public void save(Dictionary dictionary,String username){
        Person owner = personService.findByName(username);
        dictionary.setOwner(converter.convertToPersonDTO(owner));
        dictionary.setWords(new ArrayList<>());
        dictionaryRepository.save(dictionary);
    }
    public Dictionary getDictionaryByUsername(String username){
        return dictionaryRepository.findDictionariesByOwner(converter.convertToPersonDTO(personService.findByName(username)));
    }
    @Transactional
    public void deleteWords(Dictionary dictionary, List<Word> words){
        List<Word> remainingWords = getMismatchedWords(dictionary.getWords(),words);
        dictionary.setWords(remainingWords);
        dictionaryRepository.save(dictionary);
    }
    private List<Word> getMismatchedWords(List<Word> checkableWords,List<Word> filterWords){
        Map<String,String> filterValues = new HashMap<>();
        for(Word word : filterWords){
            filterValues.put(word.getValue(),word.getTranslate());
        }
        return checkableWords.stream().filter(word -> !filterValues.containsKey(word.getValue())).toList();
    }
}
