package com.example.dictionary.entities;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "Word")
public class Word {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "value")
    private String value;
    @Column(name = "translate")
    private String translate;
    @Column(name = "transcription")
    private String transcription;
    @ManyToOne
    @JoinColumn(name = "dictionary",referencedColumnName = "id")
    private Dictionary dictionary;

    public Word() {
    }

    public Word(String value, String translate, String transcription, Dictionary dictionary) {
        this.value = value;
        this.translate = translate;
        this.transcription = transcription;
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

    public String getTranscription() {
        return transcription;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }
}
