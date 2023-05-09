package com.example.dictionary.services;

import com.example.dictionary.entities.Person;
import com.example.dictionary.entities.Word;
import com.example.dictionary.repositories.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PersonService {

    private final DictionaryService dictionaryService;
    private final PersonRepository personRepository;

    public PersonService(DictionaryService dictionaryService, PersonRepository personRepository) {
        this.dictionaryService = dictionaryService;
        this.personRepository = personRepository;
    }

    public List<Person> getAllPeople(){
        return personRepository.findAll();
    }

    public List<Word> getAllWordsByPerson(int personId){
        return null;
    }
}
