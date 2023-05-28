package com.example.dictionary.services;

import com.example.dictionary.entities.Dictionary;
import com.example.dictionary.entities.Person;
import com.example.dictionary.entities.Word;
import com.example.dictionary.repositories.DictionaryRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DictionaryService {

    private final DictionaryRepository dictionaryRepository;
    private final WordService wordService;
    private final PersonService personService;

    public DictionaryService(DictionaryRepository dictionaryRepository, WordService wordService, PersonService personService) {
        this.dictionaryRepository = dictionaryRepository;
        this.wordService = wordService;
        this.personService = personService;
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
    public void addDictionary(Dictionary dictionary){
        dictionaryRepository.save(dictionary);
    }
    @Transactional
    public void deleteDictionary(Dictionary dictionary){
        dictionaryRepository.delete(dictionary);
    }
    @Transactional
    public void renameDictionary(String newName,String dictionaryId){
        Dictionary dictionary = getDictionaryById(dictionaryId);
        dictionary.setName(newName);
    }
    @Transactional
    public void addNewWordToDictionary(List<Word> words,Dictionary dictionary){
        dictionary.getWords().addAll(words);
        dictionaryRepository.save(dictionary);
    }
    @Transactional
    public void save(Dictionary dictionary,String username){
        Person owner = personService.findByName(username);
        dictionary.setOwner(owner);
        dictionary.setWords(new ArrayList<>());
        dictionaryRepository.save(dictionary);
    }
    public Dictionary getDictionaryByUsername(String username){
        Person owner = personService.findByName(username);
        return dictionaryRepository.findDictionariesByOwner(owner);
    }
}
