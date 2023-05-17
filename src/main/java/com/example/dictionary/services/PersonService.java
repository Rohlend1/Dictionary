package com.example.dictionary.services;

import com.example.dictionary.entities.Dictionary;
import com.example.dictionary.entities.Person;
import com.example.dictionary.entities.Word;
import com.example.dictionary.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PersonService {

    private final DictionaryService dictionaryService;
    private final PersonRepository personRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonService(DictionaryService dictionaryService, PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.dictionaryService = dictionaryService;
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
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

    @Transactional
    public void saveUser(Person person){
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        personRepository.save(person);
    }

    @Transactional
    public void removeUser(Person person){
        personRepository.delete(person);
    }

    @Transactional
    public void renameUser(String newUsername,int userId){
        Person person = getUserById(userId);
        person.setUsername(newUsername);
    }
}
