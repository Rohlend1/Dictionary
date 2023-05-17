package com.example.dictionary.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonIgnore
    private int id;
    @Column(name = "username")
    @NotEmpty
    @Size(min = 1,max = 40,message = "Your username is too long or too small")
    private String username;

    @JsonManagedReference
    @OneToOne(mappedBy = "owner")
    private Dictionary dictionary;

    @NotEmpty
    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    public Person(String username, Dictionary dictionary, String password, String role) {
        this.username = username;
        this.dictionary = dictionary;
        this.password = password;
        this.role = role;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}
