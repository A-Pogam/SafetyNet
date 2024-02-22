package TestController;

import com.SafetyNet.SafetyNet.SafetyNetApplication;
import controller.MedicalRecordController;
import controller.PersonController;
import dto.FireStationCoverage;
import model.MedicalRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import service.FireStationService;
import service.MedicalRecordService;
import service.PersonService;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.ArgumentMatchers.eq;




import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = SafetyNetApplication.class)
@AutoConfigureMockMvc
public class FireStationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private FireStationService fireStationService;

    @Mock
    private MedicalRecordService medicalRecordService;

    @Mock
    private PersonService personService;

    @InjectMocks
    private FireStationService fireStationController;

    @InjectMocks
    private MedicalRecordController medicalRecordController;

    @InjectMocks
    private PersonController personController;




    @Test
    public void testGetFireStationCoverage() throws Exception {
        // Mock FireStationCoverage object
        FireStationCoverage fireStationCoverage = new FireStationCoverage();

        when(fireStationService.getCoverageByStationNumber(1)).thenReturn(fireStationCoverage);

        mockMvc.perform(MockMvcRequestBuilders.get("/firestation?stationNumber=1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    public void testGetAllMedicalRecords() throws Exception {
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        // Populate medicalRecords with mock data

        when(medicalRecordService.getAllMedicalRecords()).thenReturn(medicalRecords);

        mockMvc.perform(get("/medicalRecord"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testAddMedicalRecord() throws Exception {
        MedicalRecord medicalRecord = new MedicalRecord();

        when(medicalRecordService.addMedicalRecord(any())).thenReturn(medicalRecord);

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.firstName").value(medicalRecord.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(medicalRecord.getLastName()));
    }

    @Test
    public void testUpdateMedicalRecord() throws Exception {
        // Créer un enregistrement médical simulé avec les données appropriées
        MedicalRecord existingMedicalRecord = new MedicalRecord("John", "Doe", "02-08-1990",
                Arrays.asList("ibuprofen"),
                Arrays.asList("peanut"));

        // Ajouter l'enregistrement à la liste medicalRecords
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        medicalRecords.add(existingMedicalRecord);

        when(medicalRecordService.updateMedicalRecord(eq("John"), eq("Doe"), any(MedicalRecord.class)))
                .thenAnswer(invocation -> {
                    String firstName = invocation.getArgument(0);
                    String lastName = invocation.getArgument(1);
                    MedicalRecord medicalRecordToUpdate = invocation.getArgument(2);

                    // Mettre à jour l'enregistrement médical simulé dans la liste medicalRecords
                    for (MedicalRecord record : medicalRecords) {
                        if (record.getFirstName().equals(firstName) && record.getLastName().equals(lastName)) {
                            record.setBirthdate(medicalRecordToUpdate.getBirthdate());
                            record.setMedications(medicalRecordToUpdate.getMedications());
                            record.setAllergies(medicalRecordToUpdate.getAllergies());
                            return record;
                        }
                    }
                    return null;
                });

        mockMvc.perform(put("/medicalRecord/{firstName}/{lastName}", "John", "Doe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.firstName").value(existingMedicalRecord.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(existingMedicalRecord.getLastName()));
    }



    @Test
    public void testDeleteMedicalRecord() throws Exception {
        when(medicalRecordService.deleteMedicalRecord(any(), any())).thenReturn(true);

        mockMvc.perform(delete("/medicalRecord/{firstName}/{lastName}", "John", "Doe"))
                .andExpect(status().isNoContent());
    }

}
