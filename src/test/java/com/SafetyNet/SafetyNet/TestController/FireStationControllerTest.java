package com.SafetyNet.SafetyNet.TestController;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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

import com.SafetyNet.SafetyNet.controller.FireStationController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.SafetyNet.SafetyNet.model.FireStation;
import com.SafetyNet.SafetyNet.service.contracts.IFireStationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = FireStationController.class)
public class FireStationControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IFireStationService iFireStationService;

    private FireStation firstFireStationMapping = new FireStation("Kaer Morhen", 1);
    private FireStation secondFireStationMapping = new FireStation("Cintra", 2);

    @Test
    public void getAllFireStations_returnOk() throws Exception {
        List<FireStation> firestations = new ArrayList<>(Arrays.asList(firstFireStationMapping, secondFireStationMapping));

        when(iFireStationService.getAllFireStations())
                .thenReturn(firestations);

        mockMvc.perform(get("/firestations")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].address").isNotEmpty())
                .andExpect(jsonPath("$.[*].station").isNotEmpty());
    }

    @Test
    public void getAllPersons_returnNotFound() throws Exception {
        when(iFireStationService.getAllFireStations())
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/firestations")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addMapping_returnCreated() throws Exception {
        FireStation thirdFireStationMapping = new FireStation("Vengerberg", 3);

        when(iFireStationService.addMapping(any(FireStation.class)))
                .thenReturn(thirdFireStationMapping);

        mockMvc.perform(post("/firestation")
                        .content(objectMapper.writeValueAsString(thirdFireStationMapping))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.address").value("Vengerberg"))
                .andExpect(jsonPath("$.station").value(3));
    }

    @Test
    public void addMapping_returnBadRequest() throws Exception {
        when(iFireStationService.addMapping(any(FireStation.class)))
                .thenReturn(null);

        mockMvc.perform(post("/firestation")
                        .content(objectMapper.writeValueAsString(new FireStation()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateFireStationNumber_returnOk() throws Exception {
        FireStation modifiedSecondFireStationMapping = new FireStation("Cintra", 7);

        when(iFireStationService.updateFireStationNumber(anyString(), anyInt(), anyInt()))
                .thenReturn(modifiedSecondFireStationMapping);

        mockMvc.perform(put("/firestations/{address}/{stationNumber}", "Cintra", 2)
                        .param("newStationNumber", "7")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.station").value(7));
    }

    @Test
    public void updateFireStationNumber_returnNotFound() throws Exception {
        when(iFireStationService.updateFireStationNumber(anyString(), anyInt(), anyInt()))
                .thenReturn(null);

        mockMvc.perform(put("/firestations/{address}/{stationNumber}", "Mahakam", 4)
                        .param("newStationNumber", "8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteMapping_returnNoContent() throws Exception {
        when(iFireStationService.deleteMapping(anyString(), anyInt()))
                .thenReturn(true);

        mockMvc.perform(delete("/firestations/{address}/{stationNumber}", "Kaer Morhen", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteMapping_returnNotFound() throws Exception {
        when(iFireStationService.deleteMapping(anyString(), anyInt()))
                .thenReturn(false);

        mockMvc.perform(delete("/firestations/{address}/{stationNumber}", "Mahakam", 5))
                .andExpect(status().isNotFound());
    }
}