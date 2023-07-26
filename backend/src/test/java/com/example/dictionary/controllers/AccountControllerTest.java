package com.example.dictionary.controllers;

import com.example.dictionary.dto.DictionaryDTO;
import com.example.dictionary.dto.PersonDTO;
import com.example.dictionary.entities.Person;
import com.example.dictionary.services.DictionaryService;
import com.example.dictionary.services.PersonService;
import com.example.dictionary.util.Converter;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PersonService personService;
    @Autowired
    private Converter converter;
    @Autowired
    private DictionaryService dictionaryService;

    private final String username = "test";
    private final String password = "test";
    private final String nameChangeTo="test1";

    private String jwt;
    private final String wrongJwt = "1234";
    private final String newPassword = "test1";
    private final String dictionaryName = "new_dict";

    @BeforeEach
    public void getAuthTokenAndCreateTestEntity() throws Exception {

        Person person = new Person();
        person.setUsername(username);
        person.setPassword(password);
        person.setRole("ROLE_USER");
        personService.register(person);
        String jsonRequestToCreatePerson = "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\" }";

        MvcResult authResult = mvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .content(jsonRequestToCreatePerson)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andReturn();

        String responseJson = authResult.getResponse().getContentAsString();
        JSONObject jsonResponse = new JSONObject(responseJson);
        jwt = jsonResponse.getString("jwt");

        String jsonRequestToCreateDict = "{ \"name\": \""+dictionaryName+"\"}";

        mvc.perform(MockMvcRequestBuilders.post("/dict")
                .content(jsonRequestToCreateDict)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+jwt)).andExpect(status().isOk());
    }

    @AfterEach
    public void deleteTestEntity(){
        DictionaryDTO dictionaryToDelete;

        if(personService.checkIfExists(username)){
            if((dictionaryToDelete = dictionaryService.findDictionaryByOwner(personService.findByName(username).getId())) != null){
                dictionaryService.deleteDictionary(dictionaryToDelete);
            }
            personService.deleteUser(personService.findByName(username));
        }
        if(personService.checkIfExists(nameChangeTo)){
            if((dictionaryToDelete = dictionaryService.findDictionaryByOwner(personService.findByName(nameChangeTo).getId())) != null){
                dictionaryService.deleteDictionary(dictionaryToDelete);
            }
            personService.deleteUser(personService.findByName(nameChangeTo));
        }
    }

    @Test
    public void testRenamePersonOk() throws Exception {
        renamePerson();

       MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/me")
                        .header("Authorization","Bearer "+jwt))
                        .andReturn();

        JSONObject jsonResponse = new JSONObject(result.getResponse().getContentAsString());

        assertEquals(jsonResponse.get("username"),nameChangeTo);
    }

    @Test
    public void testDeletePersonOk() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/delete")
                        .header("Authorization","Bearer "+jwt))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeletePersonWrongJwt()throws Exception{
        mvc.perform(MockMvcRequestBuilders.delete("/delete")
                        .header("Authorization",wrongJwt))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void testRenamePersonWrongJwt()throws Exception{

        mvc.perform(MockMvcRequestBuilders.patch("/rename")
                        .header("Authorization",wrongJwt))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void testRenamePersonWithAlreadyUsedName() throws Exception {
        String jsonRequest = "{ \"new_name\": \"admin\"}";

        mvc.perform(MockMvcRequestBuilders.patch("/rename")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+jwt))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetDictionaryAfterRename() throws Exception{

        renamePerson();

        MvcResult mvcAfterRenameResult = mvc.perform(MockMvcRequestBuilders.get("/dict/my_dictionary")
                .header("Authorization","Bearer "+jwt))
                .andExpect(status().isOk())
                .andReturn();
        JSONObject dictionaryAfterRename = new JSONObject(mvcAfterRenameResult.getResponse().getContentAsString());

        PersonDTO validPerson = converter.convertToPersonDTO(personService.findByName(nameChangeTo));
        String validCreatedAt = validPerson.getCreatedAt().toString();
        System.out.println(validCreatedAt);
        JSONObject owner = dictionaryAfterRename.getJSONObject("owner");

        assertEquals(dictionaryAfterRename.get("name"),"new_dict");
        assertEquals(owner.get("username"),nameChangeTo);
        assertEquals(LocalDateTime.parse((String)owner.get("createdAt")),LocalDateTime.parse(validCreatedAt.substring(0,validCreatedAt.length()-3)));
    }

    @Test
    public void testChangePasswordOk() throws Exception{
        changePassword();
    }

    @Test
    public void testChangePasswordWrongJwt()throws Exception{
        String jsonRequest = "{ \"new_password\": \""+newPassword+"\"}";

        mvc.perform(MockMvcRequestBuilders.patch("/repass")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization",wrongJwt))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAuthAfterChangingPassword() throws Exception{
        changePassword();

        String jsonRequest = "{ \"username\": \"" + username + "\", \"password\": \"" + newPassword + "\" }";

        MvcResult authResult = mvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andReturn();

        String responseJson = authResult.getResponse().getContentAsString();
        JSONObject jsonResponse = new JSONObject(responseJson);
        jwt = jsonResponse.getString("jwt");
    }

    private void changePassword() throws Exception {
        String jsonRequest = "{ \"new_password\": \""+newPassword+"\"}";

        mvc.perform(MockMvcRequestBuilders.patch("/repass")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+jwt))
                .andExpect(status().isOk());
    }

    private void renamePerson() throws Exception{
        String jsonRequest = "{ \"new_name\": \""+nameChangeTo+"\"}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.patch("/rename")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+jwt)).andReturn();

        String newJwtString = result.getResponse().getContentAsString();
        JSONObject jsonJwt = new JSONObject(newJwtString);

        jwt =jsonJwt.getString("jwt");
    }
}
