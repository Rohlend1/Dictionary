package com.example.dictionary.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Dictionary")
public class Dictionary {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "dictionary")
    private List<Word> words;

    @OneToOne(mappedBy = "dictionary")
    private Person owner;

    public Dictionary() {
    }

    public Dictionary(String name, List<Word> words, Person owner) {
        this.name = name;
        this.words = words;
        this.owner = owner;
    }

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

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }
}
