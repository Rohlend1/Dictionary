package com.example.dictionary.controllers;

import com.example.dictionary.dto.DictionaryDTO;
import com.example.dictionary.dto.WordDTO;
import com.example.dictionary.services.DictionaryService;
import com.example.dictionary.services.PersonService;
import com.example.dictionary.services.WordService;
import com.example.dictionary.util.Converter;
import com.example.dictionary.util.errors.DictionaryNotCreatedException;
import com.example.dictionary.util.ErrorResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dict")
public class DictionaryController {

    private final DictionaryService dictionaryService;
    private final PersonService personService;
    private final WordService wordService;

    @Autowired
    public DictionaryController(DictionaryService dictionaryService, PersonService personService, WordService wordService) {
        this.dictionaryService = dictionaryService;
        this.personService = personService;
        this.wordService = wordService;
    }

    @GetMapping("/words")
    public List<WordDTO> getWordsByDictionary(@RequestHeader("Authorization")String jwt){
        DictionaryDTO dictionary = getDictionaryByJwt(jwt);
        if(dictionary != null){
            return getDictionaryByJwt(jwt).getWords().stream().toList();
        }
        else {
            return new ArrayList<>();
        }
    }


    @PostMapping("")
    public ResponseEntity<HttpStatus> createDictionary(@RequestBody @Valid DictionaryDTO dictionaryDTO,
                                                       BindingResult bindingResult,
                                                       @RequestHeader("Authorization") String jwt){

        if(bindingResult.hasErrors() || dictionaryService.findDictionaryJwt(jwt) != null){
            throw new DictionaryNotCreatedException("Incorrect data");
        }
        dictionaryService.save(dictionaryDTO, jwt);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/find/my_dictionary")
    public DictionaryDTO showDictionary(@RequestHeader("Authorization") String jwt){
        if(!personService.checkIfExists(jwt)){
            throw new RuntimeException();
        }
        return dictionaryService.findDictionaryJwt(jwt);
    }

    @PostMapping("/add/words")
    public ResponseEntity<HttpStatus> addNewWords(@RequestHeader("Authorization") String jwt,
                                                 @RequestBody Map<String,List<WordDTO>> wordsDTO){
        List<WordDTO> words = wordsDTO.get("words");
        DictionaryDTO dictionary = getDictionaryByJwt(jwt);
        dictionaryService.addNewWordToDictionary(words, dictionary);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteDictionary(@RequestHeader("Authorization") String jwt){
        DictionaryDTO dictionary = getDictionaryByJwt(jwt);
        dictionaryService.deleteDictionary(dictionary);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<HttpStatus> renameDictionary(@RequestHeader("Authorization") String jwt,
                                                       @RequestBody Map<String,String> jsonData){
        DictionaryDTO dictionary = getDictionaryByJwt(jwt);
        dictionaryService.renameDictionary(dictionary, jsonData.get("newName"));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/delete/words")
    public ResponseEntity<HttpStatus> deleteWordsFromDictionary(@RequestHeader("Authorization") String jwt,
                                                                @RequestBody Map<String,List<WordDTO>> wordsDTO){
        List<WordDTO> words = wordsDTO.get("words");
        dictionaryService.deleteWords(getDictionaryByJwt(jwt), words);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/find/words")
    public List<WordDTO> findWordsByDictionary(@RequestHeader("Authorization") String jwt,
                                              @RequestParam(value = "starts_with", required = false)String startsWith,
                                               @RequestParam(value = "by_translate",required = false)Boolean findByTranslate){
        DictionaryDTO dictionary = getDictionaryByJwt(jwt);
        if(findByTranslate == null || !findByTranslate){
            return wordService.findByValue(startsWith, dictionary.getWords());
        }
        else{
            return wordService.findByTranslate(startsWith,dictionary.getWords());
        }
    }

    @GetMapping("find/excluded-words")
    public List<WordDTO> getAllWordsExcludedByDictionary(@RequestHeader("Authorization") String jwt,
                                                         @RequestParam (value = "page")int page,
                                                         @RequestParam (value = "items_per_page")int itemsPerPage){
        DictionaryDTO dictionary = getDictionaryByJwt(jwt);
        return dictionaryService.getAllWordsExcludedByDictionary(dictionary, page, itemsPerPage);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> exceptionHandler(DictionaryNotCreatedException e){
        ErrorResponse response = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    private DictionaryDTO getDictionaryByJwt(String jwt){
        return dictionaryService.findDictionaryJwt(jwt);
    }
}
