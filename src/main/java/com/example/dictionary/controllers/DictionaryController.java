package com.example.dictionary.controllers;

import com.example.dictionary.dto.DictionaryDTO;
import com.example.dictionary.dto.PersonDTO;
import com.example.dictionary.services.DictionaryService;
import com.example.dictionary.util.Converter;
import com.example.dictionary.util.DictionaryNotCreatedException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.Map;

@RestController
@RequestMapping("/dict")
public class DictionaryController {

    private final DictionaryService dictionaryService;
    private final Converter converter;

    @Autowired
    public DictionaryController(DictionaryService dictionaryService, Converter converter) {
        this.dictionaryService = dictionaryService;
        this.converter = converter;
    }

    @GetMapping("/{id}")
    public DictionaryDTO getDictionary(@PathVariable("id")int id){
        DictionaryDTO dictionaryDTO = converter.convertToDictionaryDTO(dictionaryService.getDictionaryById(id));
        System.out.println(dictionaryDTO.getOwner());
        return dictionaryDTO;
    }

    @PostMapping("")
    public ResponseEntity<HttpStatus> createDictionary(@RequestBody @Valid DictionaryDTO dictionaryDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new DictionaryNotCreatedException("Incorrect data");
        }
        dictionaryService.save(converter.convertToDictionary(dictionaryDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
