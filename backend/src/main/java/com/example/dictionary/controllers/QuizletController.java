package com.example.dictionary.controllers;

import com.example.dictionary.dto.QuizCardDTO;
import com.example.dictionary.services.QuizletService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/quizlet")
@RequiredArgsConstructor
public class QuizletController {

    private final QuizletService quizletService;

    @GetMapping
    public List<QuizCardDTO> getQuizlet(@RequestHeader("Authorization") String jwt){
        return quizletService.createQuiz();
    }
}
