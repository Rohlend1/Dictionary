package com.example.dictionary.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
public class DictionaryMetaDTO {

    private String id;

    @NotBlank(message = "Name can't be blank")
    private String name;

    private Long owner;
}
