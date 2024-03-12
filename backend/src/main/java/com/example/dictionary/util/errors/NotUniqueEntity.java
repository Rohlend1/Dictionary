package com.example.dictionary.util.errors;

public class NotUniqueEntity extends RuntimeException {
    public NotUniqueEntity() {
    }

    public NotUniqueEntity(String message) {
        super(message);
    }
}
