package com.example.dictionary.entities;

import com.example.dictionary.repositories.DictionaryRepository;
import jakarta.persistence.*;

@Entity
@Table(name = "Person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "username")
    private String username;

    @OneToOne
    @JoinColumn(name = "dictionary",referencedColumnName = "id")
    private Dictionary dictionary;

    public Person(int id, String username, Dictionary dictionary) {
        this.id = id;
        this.username = username;
        this.dictionary = dictionary;
    }

    public Person() {
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }
}
