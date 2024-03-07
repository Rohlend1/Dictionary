//package com.example.dictionary.controllers;
//
//import com.example.dictionary.entities.Person;
//import com.example.dictionary.services.PersonService;
//import org.json.JSONObject;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class HomeControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private PersonService personService;
//
//    private String jwt;
//
//    private final String username = "test";
//    private final String password = "test";
//
//    @BeforeEach
//    public void getAuthToken() throws Exception {
//        Person person = new Person();
//        person.setUsername(username);
//        person.setPassword(password);
//        person.setRole("ROLE_USER");
//        personService.register(person);
//        String jsonRequest = "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\" }";
//
//        MvcResult authResult = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
//                .content(jsonRequest)
//                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
//                .andReturn();
//
//        String responseJson = authResult.getResponse().getContentAsString();
//        JSONObject jsonResponse = new JSONObject(responseJson);
//        jwt = jsonResponse.getString("jwt");
//    }
//
//    @AfterEach
//    public void deleteTestEntity(){
//        if(personService.checkIfExists("test")){
//            personService.deleteUser(personService.findByName("test"));
//        }
//    }
//
//    @Test
//    public void testMePageWithOkRequest() throws Exception{
//        mockMvc.perform(MockMvcRequestBuilders.get("/me").header("Authorization","Bearer "+jwt)).andExpect(status().isOk());
//    }
//
//    @Test
//    public void testHomePageWithUnauthorized() throws Exception{
//        mockMvc.perform(MockMvcRequestBuilders.get("/home")).andExpect(status().isFound());
//    }
//    @Test
//    public void testMePageWithUnauthorized() throws Exception{
//        mockMvc.perform(MockMvcRequestBuilders.get("/me")).andExpect(status().isFound());
//    }
//
//    @Test
//    public void testHomePageWithOkRequest() throws Exception{
//        mockMvc.perform(MockMvcRequestBuilders.get("/home").header("Authorization","Bearer "+jwt)).andExpect(status().isOk());
//    }
//
//    @Test
//    public void testMePageWithBadToken() throws Exception{
//        mockMvc.perform(MockMvcRequestBuilders.get("/me").header("Authorization","Bearer 1"+jwt)).andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void testHomePageWithBadToken() throws Exception{
//        mockMvc.perform(MockMvcRequestBuilders.get("/home").header("Authorization","Bearer 1"+jwt))
//                .andExpect(status().isBadRequest());
//    }
//
//}
