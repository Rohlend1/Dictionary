package com.example.dictionary.controllers;


import com.example.dictionary.dto.PersonDTO;
import com.example.dictionary.entities.Dictionary;
import com.example.dictionary.security.JwtUtil;
import com.example.dictionary.services.DictionaryService;
import com.example.dictionary.services.PersonService;
import com.example.dictionary.services.WordService;
import com.example.dictionary.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("")
public class HomeController {

    private final DictionaryService dictionaryService;
    private final WordService wordService;
    private final PersonService personService;

    private final Converter converter;
     private final JwtUtil jwtUtil;

    @Autowired
    public HomeController(DictionaryService dictionaryService, WordService wordService, PersonService personService, Converter converter, JwtUtil jwtUtil) {
        this.dictionaryService = dictionaryService;
        this.wordService = wordService;
        this.personService = personService;
        this.converter = converter;
        this.jwtUtil = jwtUtil;
    }
    @GetMapping("/home")
    public Dictionary helloPage(@RequestHeader("Authorization") String jwt){
        return dictionaryService.getDictionaryByUsername(jwtUtil.validateTokenAndRetrieveClaim(jwt.substring(7)));
    }

    @GetMapping("")
    public List<PersonDTO> getAllUsers(){
        return personService.getAllUsers().stream().map(converter::convertToPersonDTO).toList();
    }
    // TODO
    public String mostPopularWords(){
        return null;
    }


}
