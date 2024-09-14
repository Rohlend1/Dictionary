package com.example.dictionary.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Document("Dictionary")
public class Dictionary {

    @Id
    @JsonIgnore
    private Long id;

    private String name;

    private List<Word> words;

    private Long owner;
}
