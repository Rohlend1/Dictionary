package com.example.dictionary.services;

import com.example.dictionary.dto.WordCardDTO;
import com.example.dictionary.entities.WordCard;
import com.example.dictionary.repositories.WordCardRepository;
import com.example.dictionary.repositories.WordRepository;
import com.example.dictionary.util.CardStatus;
import com.example.dictionary.util.Converter;
import com.example.dictionary.util.errors.NotUniqueEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WordCardServiceTest {

    @Mock
    private WordCardRepository wordCardRepository;

    @Mock
    private WordRepository wordRepository;

    @Mock
    private Converter converter;

    @InjectMocks
    private WordCardService wordCardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllShouldReturnDtoList() {
        List<WordCard> wordCards = List.of(new WordCard());
        List<WordCardDTO> dtoList = List.of(new WordCardDTO());

        when(wordCardRepository.findAll()).thenReturn(wordCards);
        when(converter.convertToWordCardDtoList(wordCards)).thenReturn(dtoList);

        List<WordCardDTO> result = wordCardService.findAll();

        assertEquals(dtoList, result);
    }

    @Test
    void voteShouldIncreaseVoteCount() {
        WordCard card = new WordCard();
        card.setVotesFor(BigInteger.valueOf(3));
        card.setVotesAgainst(BigInteger.ZERO);
        card.setVoters(new HashSet<>());

        when(wordCardRepository.findById(1L)).thenReturn(Optional.of(card));

        wordCardService.vote(1L, 100L, false);

        assertEquals(BigInteger.valueOf(4), card.getVotesFor());
        assertTrue(card.getVoters().contains(100L));
    }

    @Test
    void voteShouldIncreaseAgainstVoteCount() {
        WordCard card = new WordCard();
        card.setVotesFor(BigInteger.ZERO);
        card.setVotesAgainst(BigInteger.ZERO);
        card.setVoters(new HashSet<>());

        when(wordCardRepository.findById(1L)).thenReturn(Optional.of(card));

        wordCardService.vote(1L, 101L, true);

        assertEquals(BigInteger.ONE, card.getVotesAgainst());
        assertTrue(card.getVoters().contains(101L));
    }

    @Test
    void saveShouldThrowIfWordNotUnique() {
        WordCardDTO dto = new WordCardDTO();
        dto.setWord("test");

        when(wordCardRepository.findAllByWord("test")).thenReturn(List.of(new WordCard()));

        assertThrows(NotUniqueEntity.class, () -> wordCardService.save(dto));
    }

    @Test
    void saveShouldPersistNewWordCard() {
        WordCardDTO dto = new WordCardDTO();
        dto.setWord("unique");

        WordCard card = new WordCard();

        when(wordCardRepository.findAllByWord("unique")).thenReturn(List.of());
        when(wordRepository.findAllByValue("unique")).thenReturn(List.of());
        when(converter.convertToWordCard(dto)).thenReturn(card);

        wordCardService.save(dto);

        assertEquals(CardStatus.CREATED, card.getStatus());
        assertNotNull(card.getDecisionTime());

        verify(wordCardRepository).save(card);
    }

    @Test
    void findAllByStatusShouldReturnDtos() {
        CardStatus status = CardStatus.CREATED;
        List<WordCard> wordCards = List.of(new WordCard());
        List<WordCardDTO> dtos = List.of(new WordCardDTO());

        when(wordCardRepository.findAllByStatus(status)).thenReturn(wordCards);
        when(converter.convertToWordCardDtoList(wordCards)).thenReturn(dtos);

        List<WordCardDTO> result = wordCardService.findAllByStatus(status);

        assertEquals(dtos, result);
    }

    @Test
    void findAllForModerationShouldReturnCards() {
        LocalDateTime now = LocalDateTime.now();
        List<WordCard> cards = List.of(new WordCard());

        when(wordCardRepository.findAllByDecisionTimeBeforeAndStatus(now, CardStatus.MODERATED)).thenReturn(cards);

        List<WordCard> result = wordCardService.findAllForModeration(now);

        assertEquals(cards, result);
    }

    @Test
    void deleteAllShouldCallRepository() {
        List<WordCard> cards = List.of(new WordCard());

        wordCardService.deleteAll(cards);

        verify(wordCardRepository).deleteAll(cards);
    }
}
