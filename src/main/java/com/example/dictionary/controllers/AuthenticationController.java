package com.example.dictionary.controllers;


import com.example.dictionary.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    private final PersonService personService;

    @Autowired
    public AuthenticationController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/login")
    public String loginPage(){
        return "auth/login";
    }




}
