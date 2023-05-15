package com.example.dictionary.services;

import com.example.dictionary.entities.Dictionary;
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

    public List<Person> getAllUsers(){
        return personRepository.findAll();
    }

    public Person getUserById(int userId){
        return personRepository.findById(userId).orElse(null);
    }

    public List<Word> getAllWordsByUser(int userId){
        Dictionary dictionary = getUserById(userId).getDictionary();
        return dictionary.getWords();
    }

    public Dictionary getDictionaryByUserId(int personId){
        return getUserById(personId).getDictionary();
    }

    @Transactional(readOnly = false)
    public void saveUser(Person person){
        personRepository.save(person);
    }

    @Transactional(readOnly = false)
    public void removeUser(Person person){
        personRepository.delete(person);
    }

    @Transactional(readOnly = false)
    public void renameUser(String newUsername,int userId){
        Person person = getUserById(userId);
        person.setUsername(newUsername);
    }
}
