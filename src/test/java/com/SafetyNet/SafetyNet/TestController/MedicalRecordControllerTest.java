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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.SafetyNet.SafetyNet.controller.MedicalRecordController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.SafetyNet.SafetyNet.model.MedicalRecord;
import com.SafetyNet.SafetyNet.service.contracts.IMedicalRecordService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = MedicalRecordController.class)
public class MedicalRecordControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    private MedicalRecord firstMedicalRecord = new MedicalRecord("Geralt", "De Riv", LocalDate.parse("1171-01-01"), new ArrayList<>(Arrays.asList("Swallow potion", "Wolf potion")), new ArrayList<>());
    private MedicalRecord secondMedicalRecord = new MedicalRecord("Cirilla", "Fiona Ellen Rianon", LocalDate.parse("1251-04-30"), new ArrayList<>(Arrays.asList("Thunderbolt potion", "Blizzard potion")), new ArrayList<>());

    @MockBean
    private IMedicalRecordService iMedicalRecordService;

    @Test
    public void getAllFireStations_returnOk() throws Exception {
        List<MedicalRecord> medicalRecords = new ArrayList<>(Arrays.asList(firstMedicalRecord, secondMedicalRecord));

        when(iMedicalRecordService.getAllMedicalRecords())
                .thenReturn(medicalRecords);

        mockMvc.perform(get("/medicalRecords")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].firstName").isNotEmpty())
                .andExpect(jsonPath("$.[*].lastName").isNotEmpty())
                .andExpect(jsonPath("$.[*].birthdate").isNotEmpty())
                .andExpect(jsonPath("$.[*].medications").isNotEmpty())
                .andExpect(jsonPath("$.[*].allergies").isNotEmpty());
    }

    @Test
    public void getAllPersons_returnNotFound() throws Exception {
        when(iMedicalRecordService.getAllMedicalRecords())
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/medicalRecords")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addMapping_returnCreated() throws Exception {
        MedicalRecord thirdMedicalRecord = new MedicalRecord("Yennefer", "De Vengerberg", LocalDate.parse("1173-05-01"), new ArrayList<>(), new ArrayList<>());

        when(iMedicalRecordService.addMedicalRecord(any(MedicalRecord.class)))
                .thenReturn(thirdMedicalRecord);

        mockMvc.perform(post("/medicalRecord")
                        .content(objectMapper.writeValueAsString(thirdMedicalRecord))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Yennefer"))
                .andExpect(jsonPath("$.lastName").value("De Vengerberg"))
                .andExpect(jsonPath("$.birthdate").value("1173-05-01"))
                .andExpect(jsonPath("$.medications").isEmpty())
                .andExpect(jsonPath("$.allergies").isEmpty());
    }

    @Test
    public void addMapping_returnBadRequest() throws Exception {
        when(iMedicalRecordService.addMedicalRecord(any(MedicalRecord.class)))
                .thenReturn(null);

        mockMvc.perform(post("/medicalRecord")
                        .content(objectMapper.writeValueAsString(new MedicalRecord()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateFireStationNumber_returnOk() throws Exception {
        MedicalRecord modifiedSecondMedicalRecord = new MedicalRecord("Cirilla", "Fiona Ellen Rianon", LocalDate.parse("1251-04-30"), new ArrayList<>(Arrays.asList("Wolf Potion")), new ArrayList<>());

        when(iMedicalRecordService.updateMedicalRecord(anyString(), anyString(), any(MedicalRecord.class)))
                .thenReturn(modifiedSecondMedicalRecord);

        mockMvc.perform(put("/medicalRecords/{firstName}/{lastName}", "Cirilla", "Fiona Ellen Rianon")
                        .content(objectMapper.writeValueAsString(modifiedSecondMedicalRecord))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.medications").value(new ArrayList<>(Arrays.asList("Wolf Potion"))));
    }

    @Test
    public void updateFireStationNumber_returnNotFound() throws Exception {
        when(iMedicalRecordService.updateMedicalRecord(anyString(), anyString(), any(MedicalRecord.class)))
                .thenReturn(null);

        mockMvc.perform(put("/medicalRecords/{firstName}/{lastName}", "Triss", "Merigold")
                        .content(objectMapper.writeValueAsString(new MedicalRecord()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteMapping_returnNoContent() throws Exception {
        when(iMedicalRecordService.deleteMedicalRecord(anyString(), anyString()))
                .thenReturn(true);

        mockMvc.perform(delete("/medicalRecords/{firstName}/{lastName}", "Geralt", "De Riv"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteMapping_returnNotFound() throws Exception {
        when(iMedicalRecordService.deleteMedicalRecord(anyString(), anyString()))
                .thenReturn(false);

        mockMvc.perform(delete("/medicalRecords/{firstName}/{lastName}", "Triss", "Merigold"))
                .andExpect(status().isNotFound());
    }
}