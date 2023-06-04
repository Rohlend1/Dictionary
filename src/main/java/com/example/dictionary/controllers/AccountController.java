package com.example.dictionary.controllers;

import com.example.dictionary.entities.Person;
import com.example.dictionary.security.JwtUtil;
import com.example.dictionary.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("")
public class AccountController {
    private final PersonService personService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AccountController(PersonService personService, JwtUtil jwtUtil) {
        this.personService = personService;
        this.jwtUtil = jwtUtil;
    }

    @PatchMapping("/rename")
    public ResponseEntity<Map<String,String>> renameAccount(@RequestBody Map<String,Object> jsonData,
                                                    @RequestHeader("Authorization") String jwt){
        String newName = (String) jsonData.get("new_name");
        if(personService.checkIfExists(newName)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else{
            Person personToChange = personService.findByName(jwtUtil.validateTokenAndRetrieveClaim(jwt.substring(7)));
            personService.renameUser(newName, personToChange);
            String newJwt = jwtUtil.rewriteUsernameInToken(newName,jwt.substring(7));
            return new ResponseEntity<>(Map.of("jwt",newJwt),HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete")
    public void deleteAccount(@RequestHeader("Authorization") String jwt){
        personService.deleteUser(personService.findByName(jwtUtil.validateTokenAndRetrieveClaim(jwt.substring(7))));
    }

}
