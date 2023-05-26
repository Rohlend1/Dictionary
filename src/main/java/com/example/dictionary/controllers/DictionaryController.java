package com.example.dictionary.controllers;

import com.example.dictionary.dto.DictionaryDTO;
import com.example.dictionary.security.JwtUtil;
import com.example.dictionary.services.DictionaryService;
import com.example.dictionary.util.Converter;
import com.example.dictionary.util.DictionaryNotCreatedException;
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
    private final Converter converter;
    private final JwtUtil jwtUtil;

    @Autowired
    public DictionaryController(DictionaryService dictionaryService, Converter converter, JwtUtil jwtUtil) {
        this.dictionaryService = dictionaryService;
        this.converter = converter;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/{id}")
    public DictionaryDTO getDictionary(@PathVariable("id")int id){
        DictionaryDTO dictionaryDTO = converter.convertToDictionaryDTO(dictionaryService.getDictionaryById(id));
        System.out.println(dictionaryDTO.getOwner());
        return dictionaryDTO;
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

}
