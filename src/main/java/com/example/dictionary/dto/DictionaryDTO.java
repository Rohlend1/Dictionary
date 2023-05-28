package com.example.dictionary.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class DictionaryDTO {

    @NotEmpty
    private String name;

    private List<WordDTO> words;

    private PersonDTO owner;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WordDTO> getWords() {
        return words;
    }

    public void setWords(List<WordDTO> words) {
        this.words = words;
    }

    public PersonDTO getOwner() {
        return owner;
    }

    public void setOwner(PersonDTO owner) {
        this.owner = owner;
    }
}
