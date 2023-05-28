package com.example.dictionary.controllers;


import com.example.dictionary.dto.PersonDTO;
import com.example.dictionary.entities.Dictionary;
import com.example.dictionary.services.DictionaryService;
import com.example.dictionary.services.PersonService;
import com.example.dictionary.services.WordService;
import com.example.dictionary.util.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("")
public class HomeController {

    private final DictionaryService dictionaryService;
    private final WordService wordService;
    private final PersonService personService;

    private final Converter converter;

    @Autowired
    public HomeController(DictionaryService dictionaryService, WordService wordService, PersonService personService, Converter converter) {
        this.dictionaryService = dictionaryService;
        this.wordService = wordService;
        this.personService = personService;
        this.converter = converter;
    }
    @GetMapping("/home")
    public Dictionary helloPage(){
        return null;
//        return personService.getDictionaryByUserId(9);
    }

    @GetMapping("")
    public List<PersonDTO> getAllUsers(){
        return personService.getAllUsers().stream().map(converter::convertToPersonDTO).toList();
    }

    // TODO
    public String mostPopularWords(){
        return null;
    }


    public Dictionary showAllDictionariesByUser(@RequestBody Map<String,Object> jsonData){
        int id = (int) jsonData.get("id");
        return null;
//        return personService.getDictionaryByUserId(id);
    }

}
