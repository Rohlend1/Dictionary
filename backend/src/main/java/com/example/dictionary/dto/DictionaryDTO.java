package com.example.dictionary.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@EqualsAndHashCode
public class DictionaryDTO {

    private Long id;

    @NotBlank(message = "Name can't be blank")
    private String name;

    @EqualsAndHashCode.Exclude
    private List<WordDTO> words;

    private Long owner;
}
