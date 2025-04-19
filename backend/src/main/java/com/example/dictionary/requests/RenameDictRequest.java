package com.example.dictionary.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RenameDictRequest {
    @NotNull
    private String newName;
    @NotNull
    private String dictId;
}
