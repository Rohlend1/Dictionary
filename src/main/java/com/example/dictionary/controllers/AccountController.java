package com.example.dictionary.controllers;

import com.example.dictionary.entities.Person;
import com.example.dictionary.services.PersonService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("")
public class AccountController {
    private final PersonService personService;
    private final ModelMapper modelMapper;

    @Autowired
    public AccountController(PersonService personService, ModelMapper modelMapper) {
        this.personService = personService;
        this.modelMapper = modelMapper;
    }

    @PatchMapping("/rename")
    public Person renameAccount(@RequestBody Map<String,Object> jsonData){
        int id = (int) jsonData.get("id");
        String newName = (String) jsonData.get("newName");
        personService.renameUser(newName,id);
        return personService.getUserById(id);
    }

    @DeleteMapping("/delete")
    public void deleteAccount(@RequestBody Map<String,Object> jsonData){
        personService.deleteUser((int)jsonData.get("id"));
    }

}
