package com.example.dictionary.entities;

import com.example.dictionary.util.CardStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.type.descriptor.java.LongPrimitiveArrayJavaType;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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

    @ElementCollection
    private Set<Long> voters;
}
