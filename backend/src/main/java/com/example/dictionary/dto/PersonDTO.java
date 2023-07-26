package com.example.dictionary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@EqualsAndHashCode
public class PersonDTO {

    private Long id;

    @NotBlank(message = "Username can't be blank")
    @Size(min = 1,max = 40,message = "Your username is too long or too small")
    private String username;

    private LocalDateTime createdAt;

}
