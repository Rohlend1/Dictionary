package com.example.dictionary.entities;

import com.example.dictionary.dto.PersonDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("Dictionary")
public class Dictionary {

    @Id
    @JsonIgnore
    private String id;

    @NotEmpty
    private String name;

    private List<Word> words;

    private PersonDTO owner;

    public Dictionary() {
    }

    public Dictionary(String name, List<Word> words, PersonDTO owner) {
        this.name = name;
        this.words = words;
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    @Override
    public String toString() {
        return "Dictionary{" +
                "name='" + name + '\'' +
                '}';
    }
}
