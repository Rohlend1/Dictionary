package com.example.dictionary.controllers;


import com.example.dictionary.dto.DictionaryDTO;
import com.example.dictionary.dto.PersonDTO;
import com.example.dictionary.security.JwtUtil;
import com.example.dictionary.services.DictionaryService;
import com.example.dictionary.services.PersonService;
import com.example.dictionary.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("")
public class HomeController {

    private final DictionaryService dictionaryService;
    private final PersonService personService;
    private final Converter converter;
    private final JwtUtil jwtUtil;

    @Autowired
    public HomeController(DictionaryService dictionaryService, PersonService personService, Converter converter, JwtUtil jwtUtil) {
        this.dictionaryService = dictionaryService;
        this.personService = personService;
        this.converter = converter;
        this.jwtUtil = jwtUtil;
    }
    @GetMapping("/home")
    public DictionaryDTO helloPage(@RequestHeader("Authorization") String jwt){
        return dictionaryService.findDictionaryJwt(jwt);
    }
    @GetMapping("/me")
    public PersonDTO showMe(@RequestHeader("Authorization") String jwt){
        return converter.convertToPersonDTO(personService.findByName(jwtUtil.validateTokenAndRetrieveClaim(jwt.substring(7))));
    }
}
