package com.example.dictionary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationDTO {
    @NotBlank(message = "Username can't be blank")
    @Size(min = 1,max = 40,message = "Your username is too long or too small")
    private String username;

    @NotBlank(message = "Password can't be blank")
    private String password;
}
