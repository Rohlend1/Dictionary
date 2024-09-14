package com.example.dictionary.entities;

import com.example.dictionary.util.CardStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "word_card")
public class WordCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Long id;

    private String word;

    private String translate;

    private String link;

    @Column(name = "votes_for")
    private BigInteger votesFor;

    @Column(name = "votes_against")
    private BigInteger votesAgainst;

    @Column(name = "decision_time")
    private LocalDateTime decisionTime;

    @Enumerated(EnumType.STRING)
    private CardStatus status;
}
