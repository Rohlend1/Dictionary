package com.example.dictionary.controllers;

import com.example.dictionary.entities.Person;
import com.example.dictionary.services.PersonService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PersonService personService;

    private final String username = "test";
    private final String password = "test";
    private final String nameChangeTo="test1";

    private String jwt;
    private final String wrongJwt = "1234";
    private final String newPassword = "test1";

    @BeforeEach
    public void getAuthTokenAndCreateTestEntity() throws Exception {

        Person person = new Person(username,password,"ROLE_USER");
        personService.register(person);
        String jsonRequest = "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\" }";

        MvcResult authResult = mvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andReturn();

        String responseJson = authResult.getResponse().getContentAsString();
        JSONObject jsonResponse = new JSONObject(responseJson);
        jwt = jsonResponse.getString("jwt");
    }

    @AfterEach
    public void deleteTestEntity(){
        if(personService.checkIfExists(username)){
            personService.deleteUser(personService.findByName(username));
        }
        if(personService.checkIfExists(nameChangeTo)){
            personService.deleteUser(personService.findByName(nameChangeTo));
        }
    }

    @Test
    public void testRenamePersonOk() throws Exception {
        String jsonRequest = "{ \"new_name\": \""+nameChangeTo+"\"}";
        MvcResult result = mvc.perform(MockMvcRequestBuilders.patch("/rename")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+jwt))
                .andExpect(status().isOk()).andReturn();

        String newJwtString = result.getResponse().getContentAsString();
        JSONObject jsonJwt = new JSONObject(newJwtString);

        result = mvc.perform(MockMvcRequestBuilders.get("/me")
                        .header("Authorization","Bearer "+jsonJwt.get("jwt")))
                        .andReturn();
        JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());

        assertEquals(jsonObject.get("username"),nameChangeTo);
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
    public void testChangePasswordOk() throws Exception{
        String jsonRequest = "{ \"new_password\": \""+newPassword+"\"}";

        mvc.perform(MockMvcRequestBuilders.patch("/repass")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer "+jwt))
                .andExpect(status().isOk());
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
        testChangePasswordOk();
        String jsonRequest = "{ \"username\": \"" + username + "\", \"password\": \"" + newPassword + "\" }";

        MvcResult authResult = mvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andReturn();

        String responseJson = authResult.getResponse().getContentAsString();
        JSONObject jsonResponse = new JSONObject(responseJson);
        jwt = jsonResponse.getString("jwt");
    }
}
