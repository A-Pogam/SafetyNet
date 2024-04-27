package TestController;

import com.SafetyNet.SafetyNet.SafetyNetApplication;
import com.SafetyNet.SafetyNet.controller.FireStationController;
import com.SafetyNet.SafetyNet.controller.MedicalRecordController;
import com.SafetyNet.SafetyNet.controller.PersonController;
import com.SafetyNet.SafetyNet.dto.FireStationCoverage;
import com.SafetyNet.SafetyNet.model.FireStation;
import com.SafetyNet.SafetyNet.model.MedicalRecord;
import com.SafetyNet.SafetyNet.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;


import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.SafetyNet.SafetyNet.service.FireStationService;
import com.SafetyNet.SafetyNet.service.MedicalRecordService;
import com.SafetyNet.SafetyNet.service.PersonService;


import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.ArgumentMatchers.eq;

import java.util.List;




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
    private FireStationController fireStationController;

    @InjectMocks
    private MedicalRecordController medicalRecordController;

    @InjectMocks
    private PersonController personController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


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

   /* @Test
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
                        .content("{\"birthdate\":\"02-08-1990\",\"medications\":[\"ibuprofen\",\"aspirin\"],\"allergies\":[\"peanut\",\"pollen\"]}"))
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
    } */

   /* @Test
    public void testDeleteMapping_Success() {
        // Arrange
        String address = "123 Main St";

        // Mocking behavior
        when(fireStationService.deleteMapping(address)).thenReturn(true);

        // Act
        boolean deletionSuccessful = fireStationController.deleteMapping(address);

        // Assert
        assertTrue(deletionSuccessful);
        verify(fireStationService, times(1)).deleteMapping(address);
    }


    @Test
    public void testDeleteMapping_Failure() {
        // Arrange
        String address = "456 Elm St";

        // Mocking behavior
        when(fireStationService.deleteMapping(address)).thenReturn(false);

        // Act
        boolean deletionSuccessful = fireStationController.deleteMapping(address);

        // Assert
        assertFalse(deletionSuccessful);
        verify(fireStationService, times(1)).deleteMapping(address);
    } */

    @Test
    public void testGetAllFireStations() throws Exception {
        // Mock List of FireStation objects
        List<FireStation> fireStations = new ArrayList<>();
        // Ajoutez ici des objets FireStation à votre liste de simulation

        // Mock FireStationService
        when(fireStationService.getAllFireStations()).thenReturn(fireStations);

        // Effectuer une requête HTTP GET vers l'URL /firestations
        mockMvc.perform(MockMvcRequestBuilders.get("/firestations"))
                // S'attend à ce que le statut de la réponse soit OK (200)
                .andExpect(status().isOk());
    }

    @Test
    public void testGetFloodStations() throws Exception {
        // Mock List of FireStation objects
        List<FireStation> floodStations = new ArrayList<>();

        // Mock List of Person objects
        List<Person> residents = new ArrayList<>();

        // Mock Map of MedicalRecord objects
        Map<String, MedicalRecord> medicalRecords = new HashMap<>();

        // Mock FireStationService
        when(fireStationService.getFloodStations(anyList())).thenReturn(floodStations);

        // Mock PersonService
        when(personService.getPersonsByAddress(anyString())).thenReturn(residents);

        // Mock MedicalRecordService
        when(medicalRecordService.getMedicalRecordByName(anyString(), anyString())).thenAnswer(new Answer<MedicalRecord>() {
            @Override
            public MedicalRecord answer(InvocationOnMock invocation) throws Throwable {
                String firstName = invocation.getArgument(0);
                String lastName = invocation.getArgument(1);
                return medicalRecords.get(firstName + lastName);
            }
        });

        // Effectuer une requête HTTP GET vers l'URL /flood/stations avec les paramètres nécessaires
        mockMvc.perform(MockMvcRequestBuilders.get("/flood/stations")
                        .param("stations", "1", "2", "3"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetResidentsAndFireStation_Success() {
        // Given
        String address = "123 Main St";

        // Mocking the behavior of personService
        List<Person> residents = new ArrayList<>();
        residents.add(new Person("John", "Doe", "123 Main St", "Légume", "12", "1234", "john@exemple.com"));
        residents.add(new Person("Jane", "Doe", "12 rue de la courgette", "Légume", "12", "1234", "jane@exemple.com"));

        // Mocking the behavior of fireStationService
        FireStationService fireStationService = mock(FireStationService.class);
        PersonService personService = mock(PersonService.class);
        MedicalRecordService medicalRecordService = mock(MedicalRecordService.class);

        // Creating a list of FireStation
        List<FireStation> fireStations = new ArrayList<>();
        fireStations.add(new FireStation("123 Main St", Integer.valueOf(1)));

        // Creating the controller with mocked services and the list of FireStation
        FireStationController fireStationController = new FireStationController(fireStationService, personService, medicalRecordService, fireStations);

        // Mocking the expected behavior when calling methods on mocked services
        when(fireStationService.getFireStationNumberByAddress(address)).thenReturn(1); // Assuming the fire station number for this address is 1
        when(personService.getPersonsByAddress(address)).thenReturn(residents);

        // Calling the method to be tested
        ResponseEntity<?> responseEntity = fireStationController.getResidentsAndFireStation(address);

        // Assertions on the response
        assertEquals(200, responseEntity.getStatusCodeValue()); // Assuming the expected status code is 200
    }
    }


