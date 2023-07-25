package com.example.dictionary.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
public class WordDTO {

    private Long id;

    @NotBlank(message = "Value can't be blank")
    private String value;

    @NotBlank(message = "Translate can't be blank")
    private String translate;
}
