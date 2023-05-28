package com.example.dictionary.controllers;

import com.example.dictionary.dto.DictionaryDTO;
import com.example.dictionary.security.JwtUtil;
import com.example.dictionary.services.DictionaryService;
import com.example.dictionary.services.PersonService;
import com.example.dictionary.services.WordService;
import com.example.dictionary.util.Converter;
import com.example.dictionary.util.DictionaryNotCreatedException;
import com.example.dictionary.util.ErrorResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/dict")
public class DictionaryController {

    private final DictionaryService dictionaryService;
    private final PersonService personService;
    private final Converter converter;
    private final JwtUtil jwtUtil;
    private final WordService wordService;

    @Autowired
    public DictionaryController(DictionaryService dictionaryService, PersonService personService, Converter converter, JwtUtil jwtUtil, WordService wordService) {
        this.dictionaryService = dictionaryService;
        this.personService = personService;
        this.converter = converter;
        this.jwtUtil = jwtUtil;
        this.wordService = wordService;
    }


    @PostMapping("")
    public ResponseEntity<HttpStatus> createDictionary(@RequestBody @Valid DictionaryDTO dictionaryDTO,
                                                       BindingResult bindingResult,
                                                       @RequestHeader("Authorization") String jwt){
        String username = jwtUtil.validateTokenAndRetrieveClaim(jwt.substring(7));
        if(bindingResult.hasErrors()){
            throw new DictionaryNotCreatedException("Incorrect data");
        }
        dictionaryService.save(converter.convertToDictionary(dictionaryDTO),username);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/my_dictionary")
    public DictionaryDTO showDictionary(@RequestHeader("Authorization") String jwt){
        String username = jwtUtil.validateTokenAndRetrieveClaim(jwt.substring(7));
        if(!personService.checkIfExists(username)){
            throw new RuntimeException();
        }
        return converter.convertToDictionaryDTO(personService.findByName(username).getDictionary());
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> exceptionHandler(DictionaryNotCreatedException e){
        ErrorResponse response = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
}
