package com.SafetyNet.SafetyNet.TestController;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.SafetyNet.SafetyNet.controller.FireController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.SafetyNet.SafetyNet.service.contracts.IFireStationService;

@WebMvcTest(controllers = FireController.class)
public class FireControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IFireStationService iFireStationService;

    @Test
    public void getResidentsAndFireStationByAddress_returnOk() throws Exception {
        Map<String, Object> firstResident = new HashMap<>();
        firstResident.put("firstName", "Geralt");
        firstResident.put("lastName", "De Riv");
        firstResident.put("phone", "0102030405");
        firstResident.put("age", 94);
        firstResident.put("medication", new ArrayList<>(Arrays.asList("Swallow potion", "Wolf potion")));
        firstResident.put("allergies", new ArrayList<>());

        Map<String, Object> secondResident = new HashMap<>();
        secondResident.put("firstName", "Cirilla");
        secondResident.put("lastName", "Fiona Ellen Rianon");
        secondResident.put("phone", "0504030201");
        secondResident.put("age", 14);
        secondResident.put("medication", new ArrayList<>(Arrays.asList("Thunderbolt potion", "Blizzard potion")));
        secondResident.put("allergies", new ArrayList<>());

        List<Map<String, Object>> residents = new ArrayList<>(Arrays.asList(firstResident, secondResident));
        List<Integer> fireStationNumbers = new ArrayList<>(Arrays.asList(1));

        Map<String, Object> residentsAndFireStations = new HashMap<>();
        residentsAndFireStations.put("residents", residents);
        residentsAndFireStations.put("fireStationNumbers", fireStationNumbers);

        when(iFireStationService.getResidentsAndFireStationByAddress(anyString()))
                .thenReturn(residentsAndFireStations);

        mockMvc.perform(get("/fire")
                        .param("address", "Kaer Morhen")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.residents.[0].firstName").value("Geralt"))
                .andExpect(jsonPath("$.residents.[0].lastName").value("De Riv"))
                .andExpect(jsonPath("$.residents.[0].phone").value("0102030405"))
                .andExpect(jsonPath("$.residents.[0].age").value(94))
                .andExpect(jsonPath("$.residents.[0].medication").value(new ArrayList<>(Arrays.asList("Swallow potion", "Wolf potion"))))
                .andExpect(jsonPath("$.residents.[0].allergies").isEmpty())
                .andExpect(jsonPath("$.residents.[1]").isNotEmpty())
                .andExpect(jsonPath("$.fireStationNumbers.[0]").value(1));
    }

    @Test
    public void getResidentsAndFireStationByAddress_returnNotFound() throws Exception {
        when(iFireStationService.getResidentsAndFireStationByAddress(anyString()))
                .thenReturn(null);

        mockMvc.perform(get("/fire")
                        .param("address", "Mahakam")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

