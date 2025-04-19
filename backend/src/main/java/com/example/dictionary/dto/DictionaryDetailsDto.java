package com.example.dictionary.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@EqualsAndHashCode
public class DictionaryDetailsDto{

    private String id;
    private List<WordDTO> words;
    private Long Owner;
    private String name;
}
