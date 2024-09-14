package com.example.dictionary.services;

import com.example.dictionary.dto.DictionaryDTO;
import com.example.dictionary.dto.WordDTO;
import com.example.dictionary.entities.Dictionary;
import com.example.dictionary.entities.Person;
import com.example.dictionary.repositories.DictionaryRepository;
import com.example.dictionary.repositories.PersonRepository;
import com.example.dictionary.security.JwtUtil;
import com.example.dictionary.util.Converter;
import com.example.dictionary.util.errors.PersonNotExistsException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
@Log4j2
public class DictionaryService {

    private final DictionaryRepository dictionaryRepository;
    private final PersonRepository personRepository;
    private final Converter converter;
    private final WordService wordService;
    private final JwtUtil jwtUtil;

    @Autowired
    public DictionaryService(DictionaryRepository dictionaryRepository, PersonRepository personRepository, Converter converter, WordService wordService, JwtUtil jwtUtil) {
        this.dictionaryRepository = dictionaryRepository;
        this.personRepository = personRepository;
        this.converter = converter;
        this.wordService = wordService;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public void deleteDictionary(DictionaryDTO dictionaryDto){
        dictionaryRepository.delete(converter.convertToDictionary(dictionaryDto));
    }

    @Transactional
    public void renameDictionary(DictionaryDTO dictionary,String newName){
        dictionary.setName(newName);
        dictionaryRepository.save(converter.convertToDictionary(dictionary));
    }

    @Transactional
    public void addNewWordToDictionary(List<WordDTO> newWords,DictionaryDTO dictionary){
        List<WordDTO> remainingWords = getMismatchedWords(newWords, dictionary.getWords());
        dictionary.getWords().addAll(remainingWords);
        dictionaryRepository.save(converter.convertToDictionary(dictionary));
    }

    @Transactional
    public void save(DictionaryDTO dictionaryDTO, String jwt){
        String username = jwtUtil.validateTokenAndRetrieveClaim(jwt.substring(7));
        Person owner = personRepository.findByUsername(username).orElseThrow(RuntimeException::new);
        Dictionary dictionary = converter.convertToDictionary(dictionaryDTO);
        dictionary.setOwner(owner.getId());
        dictionary.setWords(new ArrayList<>());
        dictionaryRepository.save(dictionary);
    }

    public DictionaryDTO findById(Long id){
        Optional<Dictionary> dictionary = dictionaryRepository.findById(id);
        dictionary.orElseThrow(RuntimeException::new);
        return converter.convertToDictionaryDTO(dictionary.get());
    }

    public Long createSharingLink(String jwt){
        return findDictionaryJwt(jwt).getId();
    }

    public DictionaryDTO findDictionaryJwt(String jwt){
        String username = jwtUtil.validateTokenAndRetrieveClaim(jwt.substring(7));
        if(personRepository.findByUsername(username).isEmpty()){
            throw new PersonNotExistsException();
        }
        log.info(personRepository.findByUsername(username).get().toString());
        Dictionary dictionary = dictionaryRepository.findDictionariesByOwner(personRepository.findByUsername(username).orElseThrow(RuntimeException::new).getId());
        if(dictionary != null){
            return converter.convertToDictionaryDTO(dictionary);
        }
        return null;
    }
    
    @Transactional
    public void deleteWords(DictionaryDTO dictionary, List<WordDTO> words){
        List<WordDTO> remainingWords = getMismatchedWords(dictionary.getWords(), words);
        dictionary.setWords(remainingWords);
        dictionaryRepository.save(converter.convertToDictionary(dictionary));
    }

    public DictionaryDTO findDictionaryByOwner(Long owner){
        return converter.convertToDictionaryDTO(dictionaryRepository.findDictionariesByOwner(owner));
    }

    public List<WordDTO> getAllWordsExcludedByDictionary(DictionaryDTO dictionaryDto,int page,int itemsPerPage){
        List<WordDTO> words = getMismatchedWords(wordService.findAllPagination(page,itemsPerPage), dictionaryDto.getWords());
        return words.stream().toList();
    }

    private List<WordDTO> getMismatchedWords(List<WordDTO> checkableWords,List<WordDTO> filterWords){
        Map<String,String> filterValues = new HashMap<>();
        for(WordDTO word : filterWords){
            filterValues.put(word.getValue(),word.getTranslate());
        }
        return checkableWords.stream().filter(word -> !filterValues.containsKey(word.getValue())).toList();
    }
}
