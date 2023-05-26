package com.example.dictionary.controllers;

import com.example.dictionary.dto.WordDTO;
import com.example.dictionary.entities.Word;
import com.example.dictionary.services.WordService;
import com.example.dictionary.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/words")
public class WordController {

    private final Converter converter;
    private final WordService wordService;
    @Autowired
    public WordController(Converter converter, WordService wordService) {
        this.converter = converter;
        this.wordService = wordService;
    }

    @GetMapping("/{word}")
    public WordDTO showWordByValue(@PathVariable("word") String value){
        Word word = wordService.findWordByValue(value);
        return converter.convertToWordDTO(word);
    }

    @GetMapping("")
    public List<WordDTO> showAllWords(){
        return wordService.getAllWords().stream().map(converter::convertToWordDTO).toList();
    }

}
