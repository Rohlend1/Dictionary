package com.example.dictionary.controllers;

import com.example.dictionary.dto.DictionaryDTO;
import com.example.dictionary.dto.WordDTO;
import com.example.dictionary.entities.Dictionary;
import com.example.dictionary.entities.Word;
import com.example.dictionary.security.JwtUtil;
import com.example.dictionary.services.DictionaryService;
import com.example.dictionary.services.PersonService;
import com.example.dictionary.util.Converter;
import com.example.dictionary.util.DictionaryNotCreatedException;
import com.example.dictionary.util.ErrorResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dict")
public class DictionaryController {

    private final DictionaryService dictionaryService;
    private final PersonService personService;
    private final Converter converter;
    private final JwtUtil jwtUtil;

    @Autowired
    public DictionaryController(DictionaryService dictionaryService, PersonService personService, Converter converter, JwtUtil jwtUtil) {
        this.dictionaryService = dictionaryService;
        this.personService = personService;
        this.converter = converter;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/words")
    public List<WordDTO> getWordsByDictionary(@RequestHeader("Authorization")String jwt){
        return getDictionaryByJwt(jwt).getWords().stream().map(converter::convertToWordDTO).toList();
    }


    @PostMapping("")
    public ResponseEntity<HttpStatus> createDictionary(@RequestBody @Valid DictionaryDTO dictionaryDTO,
                                                       BindingResult bindingResult,
                                                       @RequestHeader("Authorization") String jwt){
        String username = jwtUtil.validateTokenAndRetrieveClaim(jwt.substring(7));

        if(bindingResult.hasErrors() || dictionaryService.getDictionaryByUsername(username) != null){
            throw new DictionaryNotCreatedException("Incorrect data");
        }
        dictionaryService.save(converter.convertToDictionary(dictionaryDTO),username);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/my_dictionary")
    public DictionaryDTO showDictionary(@RequestHeader("Authorization") String jwt){
        String username = jwtUtil.validateTokenAndRetrieveClaim(jwt.substring(7));
        if(!personService.checkIfExists(username)){
            throw new RuntimeException();
        }
        return converter.convertToDictionaryDTO(dictionaryService.getDictionaryByUsername(username));
    }

    @PostMapping("/add_words")
    public ResponseEntity<HttpStatus> addNewWord(@RequestHeader("Authorization") String jwt,
                                                 @RequestBody Map<String,List<WordDTO>> wordsDTO){
        List<Word> words = wordsDTO.get("words").stream().map(converter::convertToWord).toList();
        Dictionary dictionary = getDictionaryByJwt(jwt);
        dictionaryService.addNewWordToDictionary(words,dictionary);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity<HttpStatus> deleteDictionary(@RequestHeader("Authorization") String jwt){
        Dictionary dictionary = getDictionaryByJwt(jwt);
        dictionaryService.deleteDictionary(dictionary);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("")
    public ResponseEntity<HttpStatus> renameDictionary(@RequestHeader("Authorization") String jwt,
                                                       @RequestBody Map<String,String> jsonData){
        Dictionary dictionary = getDictionaryByJwt(jwt);
        dictionaryService.renameDictionary(dictionary,jsonData.get("newName"));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("delete_words")
    public ResponseEntity<HttpStatus> deleteWordsFromDictionary(@RequestHeader("Authorization") String jwt,
                                                                @RequestBody Map<String,List<WordDTO>> wordsDTO){
        List<Word> words = wordsDTO.get("words").stream().map(converter::convertToWord).toList();
        dictionaryService.deleteWords(getDictionaryByJwt(jwt),words);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> exceptionHandler(DictionaryNotCreatedException e){
        ErrorResponse response = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    private Dictionary getDictionaryByJwt(String jwt){
        String username = jwtUtil.validateTokenAndRetrieveClaim(jwt.substring(7));
        return dictionaryService.getDictionaryByUsername(username);
    }
}
