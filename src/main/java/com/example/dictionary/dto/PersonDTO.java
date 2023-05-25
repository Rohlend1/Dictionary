package com.example.dictionary.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class PersonDTO {
    private int id;
    @NotEmpty
    private String password;
    @NotEmpty
    @Size(min = 1,max = 40,message = "Your username is too long or too small")
    private String username;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
