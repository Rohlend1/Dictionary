package com.example.dictionary.controllers;

import com.example.dictionary.dto.PersonDTO;
import com.example.dictionary.entities.Person;
import com.example.dictionary.services.PersonService;
import com.example.dictionary.util.ErrorResponse;
import com.example.dictionary.util.PersonNotCreatedException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> registration(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for(FieldError error : errors){
                errorMsg.append(error.getDefaultMessage());
            }
            throw new PersonNotCreatedException(errorMsg.toString());
        }
        personService.saveUser(convertToPerson(personDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> exceptionHandler(PersonNotCreatedException e){
        ErrorResponse response = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    private Person convertToPerson(PersonDTO personDTO){
        return modelMapper.map(personDTO,Person.class);
    }
}
