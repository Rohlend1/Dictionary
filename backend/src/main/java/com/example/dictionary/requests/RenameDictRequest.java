package com.example.dictionary.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RenameDictRequest {
    String newName;
    String dictId;
}
