package com.example.dictionary.controllers;

import com.example.dictionary.dto.WordDTO;
import com.example.dictionary.entities.Word;
import com.example.dictionary.services.WordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/words")
@Slf4j
public class WordController {

    private final WordService wordService;
    @Autowired
    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    @GetMapping
    public List<WordDTO> showAllWords(@RequestHeader("Authorization")String jwt){
        return wordService.findAll();
    }

    @GetMapping("/find/all/pagination")
    public List<WordDTO> showAllWords(@RequestParam(value = "page")int page,
                                      @RequestParam(value = "items_per_page") int itemsPerPage){
        return wordService.findAllPagination(page,itemsPerPage);
    }

    @GetMapping("/find/translate-value")
    public List<WordDTO> findWords(@RequestParam("starts_with") String startsWith,
                                          @RequestParam("by_translate")Boolean byTranslate){
        if(byTranslate == null || !byTranslate){
            return wordService.findByValue(startsWith,wordService.findAll());
        }
        else{
            return wordService.findByTranslate(startsWith,wordService.findAll());
        }
    }

    @PostMapping
    public void save(@RequestHeader("Authorization") String jwt, @RequestBody Word word){
        wordService.save(word);
    }
}
