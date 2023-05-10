package com.example.dictionary.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Dictionary")
public class Dictionary {

    @Id
    @Column(name = "id")
    @JsonIgnore
    private int id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "dictionary",fetch = FetchType.LAZY)
    private List<Word> words;

    @OneToOne
    @JoinColumn(name = "owner_id",referencedColumnName = "id")
    @JsonBackReference
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

    @Override
    public String toString() {
        return "Dictionary{" +
                "name='" + name + '\'' +
                '}';
    }
}
