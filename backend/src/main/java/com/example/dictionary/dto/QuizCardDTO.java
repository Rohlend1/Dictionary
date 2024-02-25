package com.example.dictionary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizCardDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isAnswer;
    private String translate;
    private String value;
}
