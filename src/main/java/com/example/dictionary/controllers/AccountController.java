package com.example.dictionary.controllers;

import com.example.dictionary.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("")
public class AccountController {
    private final PersonService personService;

    @Autowired
    public AccountController(PersonService personService) {
        this.personService = personService;
    }

    @PatchMapping("/rename")
    public void rename(@RequestParam(value = "name",required = false)String newUserName,
                              @RequestParam(value = "id")int id){
        personService.renameUser(newUserName,id);
    }
}
