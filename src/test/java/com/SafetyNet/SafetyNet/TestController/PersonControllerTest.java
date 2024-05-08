package com.SafetyNet.SafetyNet.TestController;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.SafetyNet.SafetyNet.controller.PersonController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.SafetyNet.SafetyNet.model.Person;
import com.SafetyNet.SafetyNet.service.contracts.IPersonService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = PersonController.class)
public class PersonControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IPersonService iPersonService;

    private Person firstPerson = new Person("Geralt", "De Riv", "Kaer Morhen", "Kaer Morhen", "12345", "0102030405", "geralt.deriv@kaermorhen.kdw");
    private Person secondPerson = new Person("Cirilla", "Fiona Ellen Rianon", "Cintra", "Cintra", "54321", "0504030201", "ciri@kaermorhen.kdw");

    @Test
    public void getAllPersons_returnOk() throws Exception {
        List<Person> persons = new ArrayList<>(Arrays.asList(firstPerson, secondPerson));

        when(iPersonService.getAllPersons())
                .thenReturn(persons);

        mockMvc.perform(get("/persons")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].firstname").isNotEmpty())
                .andExpect(jsonPath("$.[*].lastname").isNotEmpty())
                .andExpect(jsonPath("$.[*].address").isNotEmpty())
                .andExpect(jsonPath("$.[*].city").isNotEmpty())
                .andExpect(jsonPath("$.[*].zip").isNotEmpty())
                .andExpect(jsonPath("$.[*].phone").isNotEmpty())
                .andExpect(jsonPath("$.[*].email").isNotEmpty());
    }

    @Test
    public void getAllPersons_returnNotFound() throws Exception {
        when(iPersonService.getAllPersons())
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/persons")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addPerson_returnCreated() throws Exception {
        Person thirdPerson = new Person("Yennefer", "De Vengerberg", "Vengerberg", "Vengerberg", "24680", "0204060800", "yennefer@vengerberg.aed");

        when(iPersonService.addPerson(any(Person.class)))
                .thenReturn(thirdPerson);

        mockMvc.perform(post("/person")
                        .content(objectMapper.writeValueAsString(thirdPerson))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstname").value("Yennefer"))
                .andExpect(jsonPath("$.lastname").value("De Vengerberg"))
                .andExpect(jsonPath("$.address").value("Vengerberg"))
                .andExpect(jsonPath("$.city").value("Vengerberg"))
                .andExpect(jsonPath("$.zip").value("24680"))
                .andExpect(jsonPath("$.phone").value("0204060800"))
                .andExpect(jsonPath("$.email").value("yennefer@vengerberg.aed"));
    }

    @Test
    public void addPerson_returnBadRequest() throws Exception {
        when(iPersonService.addPerson(any(Person.class)))
                .thenReturn(null);

        mockMvc.perform(post("/person")
                        .content(objectMapper.writeValueAsString(new Person()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updatePerson_returnOk() throws Exception {
        Person modifiedSecondPerson = new Person("Cirilla", "Fiona Ellen Rianon", "Cintra", "Cintra", "98765", "0908070605", "ciri@kaermorhen.kdw");

        when(iPersonService.updatePerson(anyString(), anyString(), any(Person.class)))
                .thenReturn(modifiedSecondPerson);

        mockMvc.perform(put("/persons/{firstName}/{lastName}", "Cirilla", "Fiona Ellen Rianon")
                        .content(objectMapper.writeValueAsString(modifiedSecondPerson))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.zip").value("98765"))
                .andExpect(jsonPath("$.phone").value("0908070605"));
    }

    @Test
    public void updatePerson_returnNotFound() throws Exception {
        when(iPersonService.updatePerson(anyString(), anyString(), any(Person.class)))
                .thenReturn(null);

        mockMvc.perform(put("/persons/{firstName}/{lastName}", "Triss", "Merigold")
                        .content(objectMapper.writeValueAsString(new Person()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deletePerson_returnNoContent() throws Exception {
        when(iPersonService.deletePerson(anyString(), anyString()))
                .thenReturn(true);

        mockMvc.perform(delete("/persons/{firstName}/{lastName}", "Geralt", "De Riv"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deletePerson_returnNotFound() throws Exception {
        when(iPersonService.deletePerson(anyString(), anyString()))
                .thenReturn(false);

        mockMvc.perform(delete("/persons/{firstName}/{lastName}", "Triss", "Merigold"))
                .andExpect(status().isNotFound());
    }
}