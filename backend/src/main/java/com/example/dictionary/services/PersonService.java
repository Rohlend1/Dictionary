package com.example.dictionary.services;

import com.example.dictionary.entities.Person;
import com.example.dictionary.repositories.PersonRepository;
import com.example.dictionary.security.JwtUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    @Autowired
    public PersonService(PersonRepository personRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public List<Person> getAllUsers(){
        return personRepository.findAll();
    }

    @Transactional
    public void register(Person person){
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        person.setCreatedAt(LocalDateTime.now());
        personRepository.save(person);
    }

    @Transactional
    public void renameUser(String newUsername, String jwt){
        Person person = findByName(jwtUtil.validateTokenAndRetrieveClaim(jwt.substring(7)));
        person.setUsername(newUsername);
    }

    @Transactional
    public void deleteUser(Person person){
        personRepository.delete(person);
    }

    public boolean checkIfExistsBy(String token){
        String username = jwtUtil.validateTokenAndRetrieveClaim(token.substring(7));
        return personRepository.findByUsername(username).isPresent();
    }

    public Person findByName(String username){
        return personRepository.findByUsername(username).orElse(null);
    }

    @Transactional
    public void changePassword(String newPassword, String jwt){
        Person person = findByName(jwtUtil.validateTokenAndRetrieveClaim(jwt.substring(7)));
        person.setPassword(passwordEncoder.encode(newPassword));
    }
}
