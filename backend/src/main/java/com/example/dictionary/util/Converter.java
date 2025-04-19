package com.example.dictionary.util;


import com.example.dictionary.dto.*;
import com.example.dictionary.entities.Dictionary;
import com.example.dictionary.entities.Person;
import com.example.dictionary.entities.Word;
import com.example.dictionary.entities.WordCard;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Converter {

    private final ModelMapper modelMapper;

    @Autowired
    public Converter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PersonDTO convertToPersonDTO(Person person){
        return modelMapper.map(person,PersonDTO.class);
    }

    public Person convertToPerson(PersonDTO personDTO){
        return modelMapper.map(personDTO,Person.class);
    }

    public DictionaryDTO convertToDictionaryDTO(Dictionary dictionary){
        DictionaryDTO dictionaryDTO = new DictionaryDTO();
        dictionaryDTO.setId(dictionary.getId());
        dictionaryDTO.setName(dictionary.getName());
        dictionaryDTO.setOwner(dictionary.getOwner());
        return dictionaryDTO;
    }

    public Dictionary convertToDictionary(DictionaryDTO dictionaryDTO){
        return modelMapper.map(dictionaryDTO,Dictionary.class);
    }

    public WordDTO convertToWordDTO(Word word){
        return modelMapper.map(word,WordDTO.class);
    }

    public Word convertToWord(WordDTO wordDTO){
        return modelMapper.map(wordDTO,Word.class);
    }

    public Person convertToPerson(AuthenticationDTO authenticationDTO){
        return modelMapper.map(authenticationDTO,Person.class);
    }

    public List<WordCard> convertToWordCardList(List<WordCardDTO> wordCardDTOS){
        return wordCardDTOS.stream().map(wordCardDTO -> modelMapper.map(wordCardDTO, WordCard.class)).toList();
    }

    public List<WordCardDTO> convertToWordCardDtoList(List<WordCard> wordCardList){
        return wordCardList.stream().map(wordCard -> modelMapper.map(wordCard, WordCardDTO.class)).toList();
    }

    public WordCard convertToWordCard(WordCardDTO wordCardDto){
        return modelMapper.map(wordCardDto, WordCard.class);
    }

    public WordCardDTO convertToWordCardDto(WordCard wordCard){
        return modelMapper.map(wordCard, WordCardDTO.class);
    }

}
