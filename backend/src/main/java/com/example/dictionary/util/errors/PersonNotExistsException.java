package com.example.dictionary.util.errors;

public class PersonNotExistsException extends RuntimeException{
    public PersonNotExistsException() {
        super("User with this name doesn't exist");
    }
}
