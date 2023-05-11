package com.example.dictionary.controllers;


import com.example.dictionary.entities.Dictionary;
import com.example.dictionary.services.DictionaryService;
import com.example.dictionary.services.UserService;
import com.example.dictionary.services.WordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("")
public class HomeController {

    private final DictionaryService dictionaryService;
    private final WordService wordService;
    private final UserService userService;

    public HomeController(DictionaryService dictionaryService, WordService wordService, UserService userService) {
        this.dictionaryService = dictionaryService;
        this.wordService = wordService;
        this.userService = userService;
    }

    @GetMapping()
    public Dictionary getHomePage(){
        return dictionaryService.getDictionaryById(1);
    }
}
