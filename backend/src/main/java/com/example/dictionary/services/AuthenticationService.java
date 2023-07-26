package com.example.dictionary.services;

import com.example.dictionary.dto.AuthenticationDTO;
import com.example.dictionary.entities.Person;
import com.example.dictionary.repositories.PersonRepository;
import com.example.dictionary.security.JwtUtil;
import com.example.dictionary.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthenticationService {

    private final PersonRepository personRepository;

    private final Converter converter;

    private final JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public AuthenticationService(PersonRepository personRepository, Converter converter, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.passwordEncoder =  passwordEncoder;
        this.personRepository = personRepository;
        this.converter = converter;
        this.jwtUtil = jwtUtil;
    }

    public boolean checkIfExists(String username){
        return personRepository.findByUsername(username).isPresent();
    }

    public String register(AuthenticationDTO dto){
        Person person = converter.convertToPerson(dto);
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        personRepository.save(person);
        return jwtUtil.generateToken(person.getUsername());
    }
}
