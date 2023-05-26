package com.example.dictionary.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "Word")
public class Word {
    @Id
    @Column(name = "id")
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "value")
    private String value;
    @Column(name = "translate")
    private String translate;
    @ManyToOne
    @JoinColumn(name = "dictionary_id",referencedColumnName = "id")
    @JsonBackReference
    private Dictionary dictionary;

    public Word() {
    }

    public Word(String value, String translate, Dictionary dictionary) {
        this.value = value;
        this.translate = translate;
        this.dictionary = dictionary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

}
