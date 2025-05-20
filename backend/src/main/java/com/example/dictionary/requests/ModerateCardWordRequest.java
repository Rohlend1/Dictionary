package com.example.dictionary.requests;

import com.example.dictionary.dto.WordCardDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ModerateCardWordRequest {

    List<Long> wordCardIds;
}
