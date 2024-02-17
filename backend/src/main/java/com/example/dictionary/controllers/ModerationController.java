package com.example.dictionary.controllers;

import com.example.dictionary.dto.CardModerationReq;
import com.example.dictionary.dto.WordCardDTO;
import com.example.dictionary.services.ModerationService;
import com.example.dictionary.services.WordCardService;
import com.example.dictionary.util.CardStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/moderation")
@RequiredArgsConstructor
public class ModerationController {

    private final WordCardService wordCardService;
    private final ModerationService moderationService;

    @GetMapping("/cards")
    public List<WordCardDTO> findAllToModeration(){
        return wordCardService.findAllByStatus(CardStatus.CREATED);
    }

    @PatchMapping
    public void moderate(@RequestBody CardModerationReq request){
        moderationService.changeWordCardStatus(request.getWord(), request.getTranslate());
    }
}
