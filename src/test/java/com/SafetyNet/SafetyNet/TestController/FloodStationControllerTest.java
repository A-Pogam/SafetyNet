package com.SafetyNet.SafetyNet.TestController;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.SafetyNet.SafetyNet.controller.FloodStationController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.SafetyNet.SafetyNet.service.contracts.IFireStationService;

@WebMvcTest(controllers = FloodStationController.class)
public class FloodStationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IFireStationService iFireStationService;

    @Test
    public void getFloodStations_returnOk() throws Exception {
        Map<String, Object> firstResident = new HashMap<>();
        firstResident.put("name", "Geralt De Riv");
        firstResident.put("phone", "0102030405");
        firstResident.put("age", 94);
        firstResident.put("medication", new ArrayList<>(Arrays.asList("Swallow potion", "Wolf potion")));
        firstResident.put("allergies", new ArrayList<>());

        Map<String, Object> secondResident = new HashMap<>();
        secondResident.put("name", "Cirilla Fiona Ellen Rianon");
        secondResident.put("phone", "0504030201");
        secondResident.put("age", 14);
        secondResident.put("medication", new ArrayList<>(Arrays.asList("Thunderbolt potion", "Blizzard potion")));
        secondResident.put("allergies", new ArrayList<>());

        List<Map<String, Object>> residents = new ArrayList<>(Arrays.asList(firstResident, secondResident));

        Map<String, Object> addressDetails = new HashMap<>();
        addressDetails.put("address", "Kaer Morhen");
        addressDetails.put("residents", residents);

        when(iFireStationService.getFloodStations(anyList()))
                .thenReturn(new ArrayList<>(Arrays.asList(addressDetails)));

        mockMvc.perform(get("/flood/stations")
                        .param("stations", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].residents.[0].name").value("Geralt De Riv"))
                .andExpect(jsonPath("$.[0].residents.[0].phone").value("0102030405"))
                .andExpect(jsonPath("$.[0].residents.[0].age").value(94))
                .andExpect(jsonPath("$.[0].residents.[0].medication").value(new ArrayList<>(Arrays.asList("Swallow potion", "Wolf potion"))))
                .andExpect(jsonPath("$.[0].residents.[0].allergies").isEmpty())
                .andExpect(jsonPath("$.[0].residents.[1]").isNotEmpty())
                .andExpect(jsonPath("$.[0].address").value("Kaer Morhen"));
    }

    @Test
    public void getFloodStations_returnNotfound() throws Exception {
        when(iFireStationService.getFloodStations(anyList()))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/flood/stations")
                        .param("stations", "7,8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
