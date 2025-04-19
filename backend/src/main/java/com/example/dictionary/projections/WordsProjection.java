package com.example.dictionary.projections;

import com.example.dictionary.entities.Word;

import java.util.List;

public interface WordsProjection {
    List<Word> getWords();
}
