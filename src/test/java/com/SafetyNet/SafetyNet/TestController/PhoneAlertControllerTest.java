package com.SafetyNet.SafetyNet.TestController;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.SafetyNet.SafetyNet.controller.PhoneAlertController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.SafetyNet.SafetyNet.service.contracts.IFireStationService;

@WebMvcTest(controllers = PhoneAlertController.class)
public class PhoneAlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IFireStationService iFireStationService;

    @Test
    public void getPhoneAlert_returnOk() throws Exception {
        List<String> phoneNumbers = new ArrayList<>(Arrays.asList("0102030405", "0504030201"));

        when(iFireStationService.getPhoneNumbersServedByFireStation(anyInt()))
                .thenReturn(phoneNumbers);

        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]").value("0102030405"))
                .andExpect(jsonPath("$.[1]").value("0504030201"));
    }

    @Test
    public void getPhoneAlert_returnNotFound() throws Exception {
        when(iFireStationService.getPhoneNumbersServedByFireStation(anyInt()))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", "7")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}