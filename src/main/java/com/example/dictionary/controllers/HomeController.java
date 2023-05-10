package com.example.dictionary.controllers;


import com.example.dictionary.entities.Person;
import com.example.dictionary.services.DictionaryService;
import com.example.dictionary.services.PersonService;
import com.example.dictionary.services.WordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("")
public class HomeController {

    private final DictionaryService dictionaryService;
    private final WordService wordService;
    private final PersonService personService;

    public HomeController(DictionaryService dictionaryService, WordService wordService, PersonService personService) {
        this.dictionaryService = dictionaryService;
        this.wordService = wordService;
        this.personService = personService;
    }

    @GetMapping()
    public List<Person> getHomePage(){
        return personService.getAllPeople();
    }
}
