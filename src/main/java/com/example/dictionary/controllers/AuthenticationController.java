package com.example.dictionary.controllers;


import com.example.dictionary.dto.AuthenticationDTO;
import com.example.dictionary.dto.PersonDTO;
import com.example.dictionary.entities.Person;
import com.example.dictionary.security.JwtUtil;
import com.example.dictionary.services.PersonService;
import com.example.dictionary.util.Converter;
import com.example.dictionary.util.ErrorResponse;
import com.example.dictionary.util.PersonNotCreatedException;
import com.example.dictionary.util.validators.PersonValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final PersonService personService;
    private final PersonValidator personValidator;
    private final JwtUtil jwtUtil;
    private final Converter converter;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationController(PersonService personService, PersonValidator personValidator, JwtUtil jwtUtil, Converter converter, AuthenticationManager authenticationManager) {
        this.personService = personService;
        this.personValidator = personValidator;
        this.jwtUtil = jwtUtil;
        this.converter = converter;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/registration")
    public ResponseEntity<Map<String,String>> registration(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult){

        Person person = converter.convertToPerson(personDTO);

        if(bindingResult.hasErrors()){
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for(FieldError error : errors){
                errorMsg.append(error.getDefaultMessage());
            }
            throw new PersonNotCreatedException(errorMsg.toString());
        }
        personService.saveUser(person);
        String token = jwtUtil.generateToken(person.getUsername());
        return new ResponseEntity<>(Map.of("jwt-token",token),HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody @Valid AuthenticationDTO authenticationDTO,BindingResult bindingResult){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(),authenticationDTO.getPassword());

        try{
            authenticationManager.authenticate(authenticationToken);
        }
        catch (BadCredentialsException e){
            throw new PersonNotCreatedException("Incorrect credentials");
        }
        String token = jwtUtil.generateToken(authenticationDTO.getUsername());
        return new ResponseEntity<>(Map.of("jwt-token",token),HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> exceptionHandler(PersonNotCreatedException e){
        ErrorResponse response = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }



}
