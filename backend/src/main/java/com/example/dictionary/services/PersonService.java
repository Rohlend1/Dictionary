package com.example.dictionary.services;

import com.example.dictionary.entities.Person;
import com.example.dictionary.repositories.PersonRepository;
import com.example.dictionary.security.JwtUtil;
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
    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
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

    public boolean checkIfExists(String username){
        return personRepository.findByUsername(username).isPresent();
    }

    public Person findByName(String username){
        return personRepository.findByUsername(username).orElse(null);
    }

    @Transactional
    public void changePassword(String newPassword, Person person){
        person.setPassword(passwordEncoder.encode(newPassword));
    }
    public Person copyPerson(Person originalPerson){
        Person copyPerson = new Person();
        copyPerson.setPassword(originalPerson.getPassword());
        copyPerson.setRole(originalPerson.getRole());
        copyPerson.setCreatedAt(originalPerson.getCreatedAt());
        copyPerson.setUsername(originalPerson.getUsername());
        copyPerson.setId(originalPerson.getId());
        return copyPerson;
    }
}
