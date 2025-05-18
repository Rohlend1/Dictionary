package com.example.dictionary.services;

import com.example.dictionary.dto.AuthenticationDTO;
import com.example.dictionary.entities.Person;
import com.example.dictionary.repositories.PersonRepository;
import com.example.dictionary.security.JwtUtil;
import com.example.dictionary.util.Converter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    private PersonRepository personRepository;
    private Converter converter;
    private JwtUtil jwtUtil;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        personRepository = mock(PersonRepository.class);
        converter = mock(Converter.class);
        jwtUtil = mock(JwtUtil.class);
        passwordEncoder = mock(PasswordEncoder.class);

        authenticationService = new AuthenticationService(
                personRepository, converter, jwtUtil, passwordEncoder);
    }

    @Test
    void testCheckIfExistsReturnsTrueWhenUserExists() {
        when(personRepository.findByUsername("user")).thenReturn(Optional.of(new Person()));

        boolean result = authenticationService.checkIfExists("user");

        assertTrue(result);
        verify(personRepository).findByUsername("user");
    }

    @Test
    void testCheckIfExistsReturnsFalseWhenUserDoesNotExist() {
        when(personRepository.findByUsername("user")).thenReturn(Optional.empty());

        boolean result = authenticationService.checkIfExists("user");

        assertFalse(result);
        verify(personRepository).findByUsername("user");
    }

    @Test
    void registerCreatesUserEncodesPasswordAndReturnsToken() {
        AuthenticationDTO dto = new AuthenticationDTO();
        dto.setUsername("testUser");
        dto.setPassword("rawPassword");

        Person person = new Person();
        person.setUsername("testUser");
        person.setPassword("rawPassword");

        when(converter.convertToPerson(dto)).thenReturn(person);
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(jwtUtil.generateToken("testUser")).thenReturn("fake.jwt.token");

        String token = authenticationService.register(dto);

        ArgumentCaptor<Person> personCaptor = ArgumentCaptor.forClass(Person.class);
        verify(personRepository).save(personCaptor.capture());

        Person savedPerson = personCaptor.getValue();
        assertEquals("encodedPassword", savedPerson.getPassword());
        assertEquals("ROLE_ADMIN", savedPerson.getRole());
        assertEquals("fake.jwt.token", token);
    }

    @Test
    void registerThrowsExceptionIfDtoIsNull() {
        assertThrows(NullPointerException.class, () -> authenticationService.register(null));
    }

    @Test
    void registerThrowsExceptionIfConverterReturnsNull() {
        AuthenticationDTO dto = new AuthenticationDTO();
        dto.setUsername("user");
        dto.setPassword("pass");

        when(converter.convertToPerson(dto)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> authenticationService.register(dto));
    }

    @Test
    void registerUsesPasswordEncoderAndJwtUtil() {
        AuthenticationDTO dto = new AuthenticationDTO();
        dto.setUsername("user");
        dto.setPassword("123");

        Person person = new Person();
        person.setUsername("user");
        person.setPassword("123");

        when(converter.convertToPerson(dto)).thenReturn(person);
        when(passwordEncoder.encode("123")).thenReturn("encoded");
        when(jwtUtil.generateToken("user")).thenReturn("jwt.token");

        authenticationService.register(dto);

        verify(passwordEncoder).encode("123");
        verify(jwtUtil).generateToken("user");
    }

    @Test
    void registerSetsAdminRoleByDefault() {
        AuthenticationDTO dto = new AuthenticationDTO();
        dto.setUsername("admin");
        dto.setPassword("admin");

        Person person = new Person();
        person.setUsername("admin");
        person.setPassword("admin");

        when(converter.convertToPerson(dto)).thenReturn(person);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPass");
        when(jwtUtil.generateToken("admin")).thenReturn("jwt");

        authenticationService.register(dto);

        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        verify(personRepository).save(captor.capture());

        assertEquals("ROLE_ADMIN", captor.getValue().getRole());
    }
}
