package com.example.dictionary.services;

import com.example.dictionary.dto.WordDTO;
import com.example.dictionary.entities.Dictionary;
import com.example.dictionary.entities.Person;
import com.example.dictionary.entities.Word;
import com.example.dictionary.repositories.DictionaryRepository;
import com.example.dictionary.util.Converter;
import com.example.dictionary.util.errors.PersonNotExistsException;
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
    private final WordService wordService;

    public DictionaryService(DictionaryRepository dictionaryRepository, PersonService personService, Converter converter, WordService wordService) {
        this.dictionaryRepository = dictionaryRepository;
        this.personService = personService;
        this.converter = converter;
        this.wordService = wordService;
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

    public Dictionary findDictionaryByUsername(String username){
        if(!personService.checkIfExists(username)){
            throw new PersonNotExistsException();
        }
        return dictionaryRepository.findDictionariesByOwner(converter.convertToPersonDTO(personService.findByName(username)));
    }

    @Transactional
    public void deleteWords(Dictionary dictionary, List<Word> words){
        List<Word> remainingWords = getMismatchedWords(dictionary.getWords(),words);
        dictionary.setWords(remainingWords);
        dictionaryRepository.save(dictionary);
    }

    @Transactional
    public void changeOwner(Person oldOwner, Person newOwner){
        Dictionary dictionary = findDictionaryByOwner(oldOwner);
        dictionary.setOwner(converter.convertToPersonDTO(newOwner));
        dictionaryRepository.save(dictionary);
    }

    public Dictionary findDictionaryByOwner(Person owner){
        return dictionaryRepository.findDictionariesByOwner(converter.convertToPersonDTO(owner));
    }

    public List<WordDTO> getAllWordsExcludedByDictionary(Dictionary dictionary,int page,int itemsPerPage){
        List<Word> words = getMismatchedWords(wordService.findAll(page,itemsPerPage),dictionary.getWords());
        return words.stream().map(converter::convertToWordDTO).toList();
    }

    private List<Word> getMismatchedWords(List<Word> checkableWords,List<Word> filterWords){
        Map<String,String> filterValues = new HashMap<>();
        for(Word word : filterWords){
            filterValues.put(word.getValue(),word.getTranslate());
        }
        return checkableWords.stream().filter(word -> !filterValues.containsKey(word.getValue())).toList();
    }
}
