package com.example.dictionary.dto;

import com.example.dictionary.entities.Word;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class DictionaryDTO {

    @NotEmpty
    private String name;

    private List<Word> words;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

}
