package com.example.dictionary.dto;

import com.example.dictionary.util.CardStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@Setter
public class WordCardDTO {

    private String word;

    private String translate;

    private String link;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigInteger votesFor;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigInteger votesAgainst;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime decisionTime;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private CardStatus status;
}
