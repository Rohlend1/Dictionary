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
@Table(name = "Dictionary")
@Entity
public class Dictionary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dictionary_id")
    private Long id;

    private String name;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "dictionary_words",
            inverseJoinColumns = {@JoinColumn(name = "dictionary_id")},
            joinColumns = {@JoinColumn(name = "word_id")}
    )
    private List<Word> words;

    @OneToOne(optional = false)
    @JoinColumn(name = "owner_id", referencedColumnName = "person_id")
    private Person owner;
}
