package com.example.dictionary.util;


import com.example.dictionary.dto.DictionaryDTO;
import com.example.dictionary.dto.PersonDTO;
import com.example.dictionary.dto.WordDTO;
import com.example.dictionary.entities.Dictionary;
import com.example.dictionary.entities.Person;
import com.example.dictionary.entities.Word;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        return modelMapper.map(dictionary,DictionaryDTO.class);
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

}
