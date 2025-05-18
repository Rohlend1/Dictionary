package com.example.dictionary.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDictionaryRequest {

    @NotBlank(message = "Name can't be blank")
    private String name;
}
