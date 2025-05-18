package com.example.dictionary.controllers;

import com.example.dictionary.dto.DictionaryDetailsDto;
import com.example.dictionary.dto.DictionaryMetaDTO;
import com.example.dictionary.dto.SharedDictionaryDto;
import com.example.dictionary.dto.WordDTO;
import com.example.dictionary.entities.Dictionary;
import com.example.dictionary.entities.Word;
import com.example.dictionary.requests.AddWordsRequest;
import com.example.dictionary.requests.CreateDictionaryRequest;
import com.example.dictionary.requests.CreateSharingLinkRequest;
import com.example.dictionary.requests.DeleteDictRequest;
import com.example.dictionary.requests.DeleteWordsRequest;
import com.example.dictionary.requests.RenameDictRequest;
import com.example.dictionary.services.DictionaryService;
import com.example.dictionary.services.PersonService;
import com.example.dictionary.services.ShareService;
import com.example.dictionary.services.WordService;
import com.example.dictionary.util.Converter;
import com.example.dictionary.util.ErrorResponse;
import com.example.dictionary.util.errors.DictionaryNotCreatedException;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dict")
@Log4j2
public class DictionaryController {

    private final DictionaryService dictionaryService;
    private final PersonService personService;
    private final WordService wordService;
    private final Converter converter;
    private final ShareService shareService;

    @Autowired
    public DictionaryController(DictionaryService dictionaryService, PersonService personService, WordService wordService, Converter converter, ShareService shareService) {
        this.dictionaryService = dictionaryService;
        this.personService = personService;
        this.wordService = wordService;
        this.converter = converter;
        this.shareService = shareService;
    }

    @GetMapping("/words")
    public List<WordDTO> getWordsByDictionary(
            @RequestHeader("Authorization") String jwt,
            @RequestParam("dict_id") String dictId) {
        return dictionaryService.findAllWordsByDict(dictId).stream().map(converter::convertToWordDTO).toList();
    }


    @PostMapping
    public ResponseEntity<HttpStatus> createDictionary(@RequestHeader("Authorization") String jwt,
                                                       @RequestBody CreateDictionaryRequest dictionaryDTO,
                                                       BindingResult bindingResult) {

        Long userId = personService.retrieveUserId(jwt);

        if (bindingResult.hasErrors()) {
            throw new DictionaryNotCreatedException("Incorrect data");
        }

        if (dictionaryService.findAll(userId).size() > 10) {
            throw new DictionaryNotCreatedException("Too many dictionaries");
        }

        dictionaryService.save(dictionaryDTO, userId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/all")
    public List<DictionaryMetaDTO> getDictionaries(@RequestHeader("Authorization") String jwt) {
        if (!personService.checkIfExistsBy(jwt)) {
            throw new RuntimeException();
        }
        return dictionaryService.findAll(jwt);
    }

    @GetMapping("/shared/{hashedOwnerId}/{token}")
    public SharedDictionaryDto getSharedDictionary(@RequestHeader("Authorization") String jwt,
                                                   @PathVariable("hashedOwnerId") Long hashedOwnerId,
                                                   @PathVariable("token") String token) {
        Dictionary dictionary = dictionaryService.findSharedDict(token, hashedOwnerId);
        if(shareService.checkIsAllowed(hashedOwnerId, dictionary.getOwner())){
            return converter.convertToSharedDictionaryDto(dictionary);
        }
        //todo перенести в конвертер shared
        throw new RuntimeException();
    }

    @PostMapping("/add/words")
    public ResponseEntity<HttpStatus> addNewWords(@RequestHeader("Authorization") String jwt,
                                                  @RequestBody AddWordsRequest req) {
        List<Word> words = req.getWords().stream().map(converter::convertToWord).toList();
        dictionaryService.addNewWordToDictionary(words, req.getDictId());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteDictionary(@RequestHeader("Authorization") String jwt, @RequestBody DeleteDictRequest req) {
        dictionaryService.deleteDictionary(req.getDictId(), jwt);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<HttpStatus> renameDictionary(@RequestHeader("Authorization") String jwt,
                                                       @RequestBody RenameDictRequest req) {
        dictionaryService.renameDictionary(req.getDictId(), req.getNewName());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/delete/words")
    public ResponseEntity<HttpStatus> deleteWordsFromDictionary(@RequestHeader("Authorization") String jwt,
                                                                @RequestBody @Valid DeleteWordsRequest req) {
        List<Word> words = req.getWords().stream().map(converter::convertToWord).toList();
        dictionaryService.deleteWords(req.getDictId(), words);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //Todo проверить есть ли возможность аггрегировать данные прямо в запросе
    // или сделать сортировку
    @GetMapping("/find/words")
    public List<WordDTO> findWordsByDictionary(@RequestHeader("Authorization") String jwt,
                                               @RequestParam(value = "starts_with", required = false) String startsWith,
                                               @RequestParam(value = "by_translate", required = false, defaultValue = "false") Boolean findByTranslate,
                                               @RequestParam(value = "dict_id") String dictId) {
        List<Word> words = dictionaryService.findAllWordsByDict(dictId);
        return wordService.findBy(startsWith, words, findByTranslate).stream().map(converter::convertToWordDTO).toList();
    }

    @GetMapping("/find/excluded-words")
    public List<WordDTO> getAllWordsExcludedByDictionary(@RequestHeader("Authorization") String jwt,
                                                         @RequestParam(value = "page") int page,
                                                         @RequestParam(value = "items_per_page") int itemsPerPage,
                                                         @RequestParam(value = "dict_id") String dictId) {
        return dictionaryService.findAllWordsExcludedByDictionary(dictId, page, itemsPerPage);
    }

    @PostMapping("/share")
    public String getSharingLink(@RequestHeader("Authorization") String jwt, @RequestBody CreateSharingLinkRequest req){
        DictionaryMetaDTO dictionaryDTO = dictionaryService.findById(req.getDictId());
        return shareService.createSharingLink(dictionaryDTO);
    }

    @GetMapping("/{dictId}")
    public DictionaryDetailsDto getDictionaryDetails(@RequestHeader("Authorization") String jwt,
                                                     @PathVariable("dictId") String dictId){
        return converter.convertToDictionaryDetailsDto(dictionaryService.getDictionaryDetails(dictId));
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> exceptionHandler(DictionaryNotCreatedException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
