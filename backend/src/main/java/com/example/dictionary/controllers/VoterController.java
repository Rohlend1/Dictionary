package com.example.dictionary.controllers;

import com.example.dictionary.dto.WordCardDTO;
import com.example.dictionary.services.PersonService;
import com.example.dictionary.services.WordCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/votes")
@RequiredArgsConstructor
public class VoterController {

    private final WordCardService wordCardService;
    private final PersonService personService;

    @GetMapping
    public List<WordCardDTO> findAll(@RequestHeader("Authorization") String jwt){
        return wordCardService.findAll();
    }

    @PatchMapping("/{id}")
    public void vote(@RequestHeader("Authorization") String jwt,
                     @PathVariable("id") Long id,
                     @RequestParam("against") Boolean isAgainst){
        Long userId = personService.retrieveUserId(jwt);
        wordCardService.vote(id, userId, isAgainst);
    }

    @PostMapping
    public void save(@RequestHeader("Authorization")String jwt, @RequestBody WordCardDTO wordCard){
        wordCardService.save(wordCard);
    }
}
