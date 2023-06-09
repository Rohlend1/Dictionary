package com.example.dictionary.controllers;

import com.example.dictionary.dto.WordDTO;
import com.example.dictionary.services.WordService;
import com.example.dictionary.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("")
    public List<WordDTO> showAllWords(@RequestParam(value = "page")int page,
                                      @RequestParam(value = "items_per_page") int itemsPerPage){
        return wordService.findAll(page,itemsPerPage).stream().map(converter::convertToWordDTO).toList();
    }

    @GetMapping("/search")
    public List<WordDTO> findWords(@RequestParam("starts_with") String startsWith,
                                          @RequestParam("by_translate")Boolean byTranslate){
        if(byTranslate == null || !byTranslate){
            return wordService.findByValue(startsWith,wordService.findAll());
        }
        else{
            return wordService.findByTranslate(startsWith,wordService.findAll());
        }
    }
}
