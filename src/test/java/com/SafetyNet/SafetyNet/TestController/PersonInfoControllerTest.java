package com.SafetyNet.SafetyNet.TestController;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.SafetyNet.SafetyNet.controller.PersonInfoController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.SafetyNet.SafetyNet.service.contracts.IPersonService;

@WebMvcTest(controllers = PersonInfoController.class)
public class PersonInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IPersonService iPersonService;

    @Test
    public void getPersonInfo_returnOk() throws Exception {
        Map<String, Object> personInfo = new HashMap<>();
        personInfo.put("name", "Geralt De Riv");
        personInfo.put("phone", "0102030405");
        personInfo.put("email", "geralt.deriv@kaermorhen.kdw");
        personInfo.put("address", "Kaer Morhen");
        personInfo.put("age", 94);
        personInfo.put("medication", new ArrayList<>(Arrays.asList("Swallow potion", "Wolf potion")));
        personInfo.put("allergies", new ArrayList<>());

        MultiValueMap<String, String> firstNameAndLastName = new LinkedMultiValueMap<>();
        firstNameAndLastName.add("firstName", "Geralt");
        firstNameAndLastName.add("lastName", "De Riv");

        when(iPersonService.getPersonInfo(anyString(), anyString()))
                .thenReturn(personInfo);

        mockMvc.perform(get("/personInfo")
                        .params(firstNameAndLastName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Geralt De Riv"))
                .andExpect(jsonPath("$.phone").value("0102030405"))
                .andExpect(jsonPath("$.email").value("geralt.deriv@kaermorhen.kdw"))
                .andExpect(jsonPath("$.address").value("Kaer Morhen"))
                .andExpect(jsonPath("$.age").value(94))
                .andExpect(jsonPath("$.medication").value(new ArrayList<>(Arrays.asList("Swallow potion", "Wolf potion"))))
                .andExpect(jsonPath("$.allergies").isEmpty());
    }

    @Test
    public void getPersonInfo_returnNotFound() throws Exception {
        MultiValueMap<String, String> firstNameAndLastName = new LinkedMultiValueMap<>();
        firstNameAndLastName.add("firstName", "Triss");
        firstNameAndLastName.add("lastName", "Merigold");

        when(iPersonService.getPersonInfo(anyString(), anyString()))
                .thenReturn(null);

        mockMvc.perform(get("/personInfo")
                        .params(firstNameAndLastName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}