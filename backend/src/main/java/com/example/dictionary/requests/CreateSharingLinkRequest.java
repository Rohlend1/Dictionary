package com.example.dictionary.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateSharingLinkRequest {
    @NotNull
    private String dictId;
}
