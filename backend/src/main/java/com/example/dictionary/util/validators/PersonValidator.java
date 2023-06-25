package com.example.dictionary.util.validators;

import com.example.dictionary.entities.Person;
import com.example.dictionary.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class PersonValidator implements Validator {

    private final PersonService personService;

    @Autowired
    public PersonValidator(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        if(!checkIfExist(person)){
            errors.rejectValue("username","","This username has already been used");
        }
    }

    private boolean checkIfExist(Person person){
        List<Person> people = personService.getAllUsers();
        return people.stream().noneMatch(p -> p.getUsername().equals(person.getUsername()));
    }
}
