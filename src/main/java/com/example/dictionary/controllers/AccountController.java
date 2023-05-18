package com.example.dictionary.controllers;

import com.example.dictionary.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("")
public class AccountController {
    private final PersonService personService;

    @Autowired
    public AccountController(PersonService personService) {
        this.personService = personService;
    }

    @PatchMapping("/rename")
    public void renameAccount(@RequestBody Map<String,Object> jsonData){
        int id = (int) jsonData.get("id");
        String newName = (String) jsonData.get("newName");
        personService.renameUser(newName,id);
    }

    @DeleteMapping("/delete")
    public void deleteAccount(@RequestBody int id){
        personService.deleteUser(id);
    }
}
