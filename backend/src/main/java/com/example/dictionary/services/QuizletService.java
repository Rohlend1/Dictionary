package com.example.dictionary.services;

import com.example.dictionary.dto.QuizCardDTO;
import com.example.dictionary.dto.WordDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class QuizletService {

    private final WordService wordService;

    public List<QuizCardDTO> createQuiz() {
        List<WordDTO> words = wordService.findAll();
        Random rand = new Random();
        List<QuizCardDTO> resultQuiz = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            QuizCardDTO quizCardDTO = new QuizCardDTO();
            WordDTO word = words.get(rand.nextInt(words.size()));
            quizCardDTO.setIsAnswer(false);
            quizCardDTO.setTranslate(word.getTranslate());
            quizCardDTO.setValue(word.getValue());
            resultQuiz.add(quizCardDTO);
        }
        resultQuiz.get(rand.nextInt(4)).setIsAnswer(true);
        return resultQuiz;
    }
}
