package com.example.dictionary.controllers;


import com.example.dictionary.services.DictionaryService;
import com.example.dictionary.services.PersonService;
import com.example.dictionary.services.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("")
public class HomeController {

    private final DictionaryService dictionaryService;
    private final WordService wordService;
    private final PersonService personService;

    @Autowired
    public HomeController(DictionaryService dictionaryService, WordService wordService, PersonService personService) {
        this.dictionaryService = dictionaryService;
        this.wordService = wordService;
        this.personService = personService;
    }
    @GetMapping("/hello")
    public String helloPage(){
        return "Hello!";
    }
    @GetMapping("/admin")
    public String adminPage(){
        return "admin!";
    }

}
