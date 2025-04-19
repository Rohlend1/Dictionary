package com.example.dictionary.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class SharedDictionaryDto {
    private List<WordDTO> words;
    private String name;
}
