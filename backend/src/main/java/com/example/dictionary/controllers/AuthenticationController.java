package com.example.dictionary.controllers;


import com.example.dictionary.dto.AuthenticationDTO;
import com.example.dictionary.security.JwtUtil;
import com.example.dictionary.services.AuthenticationService;
import com.example.dictionary.util.ErrorResponse;
import com.example.dictionary.util.errors.PersonNotCreatedException;
import jakarta.validation.Valid;
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

    private final AuthenticationService authenticationService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.authenticationService = authenticationService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/registration")
    public ResponseEntity<Map<String,String>> registration(@RequestBody @Valid AuthenticationDTO authenticationDTO, BindingResult bindingResult){

        if(authenticationService.checkIfExists(authenticationDTO.getUsername())){
            bindingResult.reject("Username exists","Name already exists");
        }
        if(bindingResult.hasErrors()){
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for(FieldError error : errors){
                errorMsg.append(error.getDefaultMessage());
            }
            throw new PersonNotCreatedException(errorMsg.toString());
        }
        String token = authenticationService.register(authenticationDTO);

        return new ResponseEntity<>(Map.of("jwt", token),HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody @Valid AuthenticationDTO authenticationDTO,BindingResult bindingResult){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(),authenticationDTO.getPassword());
        if(bindingResult.hasErrors()) {
            throw new BadCredentialsException("Incorrect data");
        }
        try{
            authenticationManager.authenticate(authenticationToken);
        }
        catch (BadCredentialsException e){
            throw new PersonNotCreatedException("Incorrect credentials");
        }
        String token = jwtUtil.generateToken(authenticationDTO.getUsername());
        return new ResponseEntity<>(Map.of("jwt",token),HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> exceptionHandler(PersonNotCreatedException e){
        ErrorResponse response = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
}
