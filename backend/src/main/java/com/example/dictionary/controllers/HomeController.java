package com.example.dictionary.controllers;


import com.example.dictionary.dto.DictionaryDTO;
import com.example.dictionary.dto.PersonDTO;
import com.example.dictionary.security.JwtUtil;
import com.example.dictionary.services.DictionaryService;
import com.example.dictionary.services.PersonService;
import com.example.dictionary.util.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class HomeController {

    private final DictionaryService dictionaryService;
    private final PersonService personService;
    private final Converter converter;
    private final JwtUtil jwtUtil;

    @GetMapping("/home")
    public List<DictionaryDTO> helloPage(@RequestHeader("Authorization") String jwt){
        return dictionaryService.findAll(jwt);
    }
    @GetMapping("/me")
    public PersonDTO showMe(@RequestHeader("Authorization") String jwt){
        return converter.convertToPersonDTO(personService.findByName(jwtUtil.validateTokenAndRetrieveClaim(jwt.substring(7))));
    }
}
