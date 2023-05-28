package com.example.dictionary.dto;

import com.example.dictionary.entities.Dictionary;


public class WordDTO {
    private String value;
    private String translate;

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

}
