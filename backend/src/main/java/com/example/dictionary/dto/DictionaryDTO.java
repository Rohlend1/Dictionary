package com.example.dictionary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@EqualsAndHashCode
public class DictionaryDTO {

    @NotBlank(message = "Name can't be blank")
    private String name;

    @EqualsAndHashCode.Exclude
    private List<WordDTO> words;

    @NotNull(message = "Owner can't be null")
    private Long owner;
}
