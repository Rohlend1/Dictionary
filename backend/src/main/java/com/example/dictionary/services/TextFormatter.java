package com.example.dictionary.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TextFormatter {

    private static final String FTS_RESPONSE_WORD_CARD_PATTERN = "Text: %s\nMain document word is %s";
    private static final String FTS_RESPONSE_TEXT_EXAMPLE_PATTERN = "Text: %s\nThis is text example";

    public static String formatFtsResponse(String text, String mainWord){
        if(mainWord == null || mainWord.isBlank())
            return String.format(FTS_RESPONSE_TEXT_EXAMPLE_PATTERN, text);
        return String.format(FTS_RESPONSE_WORD_CARD_PATTERN, text, mainWord);
    }

}
