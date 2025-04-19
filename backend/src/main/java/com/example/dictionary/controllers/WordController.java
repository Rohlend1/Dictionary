package com.example.dictionary.controllers;

import com.example.dictionary.dto.WordDTO;
import com.example.dictionary.entities.Word;
import com.example.dictionary.services.WordService;
import com.example.dictionary.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/words")
public class WordController {

    private final WordService wordService;
    private final Converter converter;

    @Autowired
    public WordController(WordService wordService, Converter converter) {
        this.wordService = wordService;
        this.converter = converter;
    }

    @GetMapping
    public List<WordDTO> showAllWords(@RequestHeader("Authorization") String jwt) {
        return wordService.findAll().stream().map(converter::convertToWordDTO).toList();
    }

    @GetMapping("/find/all/pagination")
    public List<WordDTO> showAllWords(@RequestParam(value = "page") int page,
                                      @RequestParam(value = "items_per_page") int itemsPerPage) {
        return wordService.findAllPagination(page, itemsPerPage).stream().map(converter::convertToWordDTO).toList();
    }

    @GetMapping("/find/translate-value")
    public List<WordDTO> findWords(@RequestParam("starts_with") String startsWith,
                                   @RequestParam("by_translate") Boolean byTranslate) {
        return wordService.findBy(startsWith, wordService.findAll(), byTranslate).stream().map(converter::convertToWordDTO).toList();
    }

    @PostMapping
    public void save(@RequestHeader("Authorization") String jwt, @RequestBody Word word) {
        wordService.save(word);
    }
}
