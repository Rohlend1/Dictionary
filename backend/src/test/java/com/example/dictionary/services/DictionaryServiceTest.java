package com.example.dictionary.services;

import com.example.dictionary.dto.DictionaryMetaDTO;
import com.example.dictionary.dto.WordDTO;
import com.example.dictionary.entities.Dictionary;
import com.example.dictionary.entities.Word;
import com.example.dictionary.projections.WordsProjection;
import com.example.dictionary.repositories.DictionaryRepository;
import com.example.dictionary.requests.CreateDictionaryRequest;
import com.example.dictionary.security.JwtUtil;
import com.example.dictionary.util.Converter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DictionaryServiceTest {

    private DictionaryRepository dictionaryRepository;
    private PersonService personService;
    private Converter converter;
    private WordService wordService;
    private JwtUtil jwtUtil;
    private DictionaryService dictionaryService;

    @BeforeEach
    void setUp() {
        dictionaryRepository = mock(DictionaryRepository.class);
        personService = mock(PersonService.class);
        converter = mock(Converter.class);
        wordService = mock(WordService.class);
        jwtUtil = mock(JwtUtil.class);
        dictionaryService = new DictionaryService(dictionaryRepository, converter, wordService, personService, jwtUtil);
    }

    @Test
    void testDeleteDictionaryWithValidOwner() {
        Dictionary dict = new Dictionary();
        dict.setOwner(1L);
        when(dictionaryRepository.findById("123")).thenReturn(Optional.of(dict));

        dictionaryService.deleteDictionary("123", 1L);

        verify(dictionaryRepository).delete(dict);
    }

    @Test
    void testDeleteDictionaryWithInvalidOwnerThrowsException() {
        Dictionary dict = new Dictionary();
        dict.setOwner(2L);
        when(dictionaryRepository.findById("123")).thenReturn(Optional.of(dict));

        assertThrows(RuntimeException.class, () -> dictionaryService.deleteDictionary("123", 1L));
    }

    @Test
    void testRenameDictionaryChangesName() {
        Dictionary dict = new Dictionary();
        dict.setName("Old Name");
        when(dictionaryRepository.findById("456")).thenReturn(Optional.of(dict));

        dictionaryService.renameDictionary("456", "New Name");

        assertEquals("New Name", dict.getName());
        verify(dictionaryRepository).save(dict);
    }

    @Test
    void testAddNewWordToDictionaryAddsWords() {
        Dictionary dict = new Dictionary();
        Word apple = new Word();
        apple.setId(0L);
        apple.setTranslate("яблоко");
        apple.setValue("apple");
        Word banana = new Word();
        banana.setId(1L);
        banana.setTranslate("банана");
        banana.setValue("banana");
        dict.setWords(new ArrayList<>(List.of(apple)));

        List<Word> newWords = List.of(banana);

        when(dictionaryRepository.findById("dict1")).thenReturn(Optional.of(dict));

        dictionaryService.addNewWordToDictionary(newWords, "dict1");

        assertEquals(2, dict.getWords().size());
        verify(dictionaryRepository).save(dict);
    }

    @Test
    void testSaveStoresDictionaryWithOwner() {
        CreateDictionaryRequest request = new CreateDictionaryRequest();
        Dictionary dict = new Dictionary();
        when(converter.convertToDictionary(request)).thenReturn(dict);

        dictionaryService.save(request, 42L);

        assertEquals(42L, dict.getOwner());
        verify(dictionaryRepository).save(dict);
    }

    @Test
    void testFindByIdReturnsConvertedDTO() {
        Dictionary dict = new Dictionary();
        DictionaryMetaDTO dto = new DictionaryMetaDTO();

        when(dictionaryRepository.findById("id1")).thenReturn(Optional.of(dict));
        when(converter.convertToDictionaryDTO(dict)).thenReturn(dto);

        DictionaryMetaDTO result = dictionaryService.findById("id1");

        assertEquals(dto, result);
    }

    @Test
    void testFindAllReturnsUserDictionaries() {
        Dictionary dict = new Dictionary();
        DictionaryMetaDTO dto = new DictionaryMetaDTO();

        when(dictionaryRepository.findAllByOwner(7L)).thenReturn(List.of(dict));
        when(converter.convertToDictionaryDTO(dict)).thenReturn(dto);

        List<DictionaryMetaDTO> result = dictionaryService.findAll(7L);

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void testFindSharedDictReturnsDictionary() {
        Dictionary dict = new Dictionary();
        when(jwtUtil.validateSharedTokenAndRetrieveClaim("token")).thenReturn("dict1");
        when(dictionaryRepository.findById("dict1")).thenReturn(Optional.of(dict));

        Dictionary result = dictionaryService.findSharedDict("token", 0L);

        assertEquals(dict, result);
    }

    @Test
    void testDeleteWordsRemovesMatchingWords() {
        Word apple = new Word();
        apple.setId(0L);
        apple.setTranslate("яблоко");
        apple.setValue("apple");
        Word banana = new Word();
        banana.setId(1L);
        banana.setTranslate("банана");
        banana.setValue("banana");


        Dictionary dict = new Dictionary();
        dict.setWords(new ArrayList<>(List.of(apple, banana)));

        when(dictionaryRepository.findById("dict")).thenReturn(Optional.of(dict));
        Word newBanana = new Word();
        newBanana.setTranslate("банана");
        newBanana.setValue("banana");
        dictionaryService.deleteWords("dict", List.of(newBanana));

        assertEquals(1, dict.getWords().size());
        assertFalse(dict.getWords().stream().anyMatch(w -> w.getValue().equals("banana")));
        verify(dictionaryRepository).save(dict);
    }

    @Test
    void testFindAllWordsExcludedByDictionaryReturnsCorrectWords() {
        Word apple = new Word();
        apple.setId(0L);
        apple.setTranslate("яблоко");
        apple.setValue("apple");
        Word banana = new Word();
        banana.setId(1L);
        banana.setTranslate("банана");
        banana.setValue("banana");
        Word cherry = new Word();
        banana.setId(2L);
        banana.setTranslate("вишня");
        banana.setValue("cherry");
        WordsProjection wordsProjection = new WordsProjection() {
            @Override
            public List<Word> getWords() {
                return List.of(apple);
            }
        };

        Dictionary dict = new Dictionary();
        dict.setWords(List.of(apple));

        when(dictionaryRepository.findWordsById("dict1")).thenReturn( Optional.of(wordsProjection));
        when(wordService.findAllPagination(0, 10)).thenReturn(List.of(apple,banana,cherry));
        when(converter.convertToWordDTO(banana)).thenReturn(new WordDTO());
        when(converter.convertToWordDTO(cherry)).thenReturn(new WordDTO());

        List<WordDTO> result = dictionaryService.findAllWordsExcludedByDictionary("dict1", 0, 10);

        assertEquals(2, result.size());
    }

    @Test
    void testGetDictionaryDetailsReturnsDictionary() {
        Dictionary dict = new Dictionary();
        when(dictionaryRepository.findById("dict1")).thenReturn(Optional.of(dict));

        Dictionary result = dictionaryService.getDictionaryDetails("dict1");

        assertEquals(dict, result);
    }
}
