package com.example.dictionary.dto;

import com.example.dictionary.entities.Word;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class DictionaryDTO {

    private int id;

    @NotEmpty
    private String name;

    private List<Word> words;

    private PersonDTO owner;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public PersonDTO getOwner() {
        return owner;
    }

    public void setOwner(PersonDTO owner) {
        this.owner = owner;
    }
}
