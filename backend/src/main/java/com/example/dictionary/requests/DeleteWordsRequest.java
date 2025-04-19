package com.example.dictionary.requests;

import com.example.dictionary.dto.WordDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DeleteWordsRequest {
    @NotNull
    String dictId;
    @NotNull
    List<WordDTO> words;
}
