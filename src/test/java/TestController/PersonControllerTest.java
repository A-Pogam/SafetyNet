package TestController;

import com.SafetyNet.SafetyNet.SafetyNetApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import service.FireStationService;
import service.PersonService;
import model.Person;
import model.MedicalRecord;
import service.MedicalRecordService;
import controller.PersonController;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;


@SpringBootTest(classes = SafetyNetApplication.class)
@AutoConfigureMockMvc
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;
    private MedicalRecordService medicalRecordService;
    private FireStationService fireStationService;
    private PersonController personController;

    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        medicalRecordService = Mockito.mock(MedicalRecordService.class);
        fireStationService = Mockito.mock(FireStationService.class);
        personController = new PersonController(personService, medicalRecordService, fireStationService);
    }

    @Test
    void testGetAllPersons() throws Exception {
        mockMvc.perform(get("/person"))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddPerson() throws Exception {
        // Créer une nouvelle personne
        Person person = new Person("John", "Doe", "123 Main St", "Anytown", "12345", "555-555-5555", "john@example.com");
        String personJson = "{\"firstname\":\"John\",\"lastname\":\"Doe\",\"address\":\"123 Main St\",\"city\":\"Anytown\",\"zip\":\"12345\",\"phone\":\"555-555-5555\",\"email\":\"john@example.com\"}";

        // Mock du service pour ajouter une personne
        when(personService.addPerson(any(Person.class))).thenReturn(person); // Utilisation de any()

        // Tester l'ajout de personne via le contrôleur
        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJson))
                .andExpect(status().isCreated()) // Vérifier le statut créé (201)
                .andExpect(content().string("Person added successfully")); // Vérifier le message de réussite

    }


    @Test
    void testUpdatePerson() throws Exception {
        Person updatedPerson = new Person("John", "Doe", "123 Main St", "Springfield", "12345", "123-456-7890", "john.doe@example.com");

        when(personService.updatePerson(any(Person.class))).thenReturn(updatedPerson); // Utilisation de any()

        mockMvc.perform(put("/person/John/Doe")  // Utilisation de l'URI pour mettre à jour une personne
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPerson)))
                .andExpect(status().isOk())
                .andExpect(content().string("Person updated successfully"));
    }


    @Test
    void testDeletePerson() throws Exception {
        when(personService.deletePerson("John", "Doe")).thenReturn(true);

        mockMvc.perform(delete("/person/John/Doe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Person("John", "Doe", null, null, null, null, null))))
                .andExpect(status().isNoContent())
                .andExpect(content().string("Person deleted successfully"));
    }

    @Test
    public void testGetPersonInfo() {
        // Créer un objet Person
        Person person = new Person("John", "Doe", "123 Main St", "Wonderland", "42", "1234567890", "john@example.com");

        // Créer un objet MedicalRecord
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setBirthdate("01/01/1990");
        List<String> medications = new ArrayList<>();
        medications.add("Medicine1");
        medications.add("Medicine2");
        medicalRecord.setMedications(medications);

        List<String> allergies = new ArrayList<>();
        allergies.add("Pollen");
        medicalRecord.setAllergies(allergies);

        // Définir le comportement des services mockés
        when(personService.getPersonByName("John", "Doe")).thenReturn(person);
        when(medicalRecordService.getMedicalRecordByName("John", "Doe")).thenReturn(medicalRecord);
        when(fireStationService.calculateAge("01/01/1990")).thenReturn(32); // Mocking the age calculation

        // Appeler la méthode à tester
        ResponseEntity<?> responseEntity = personController.getPersonInfo("John", "Doe");

        // Vérifier si la réponse est OK
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Vérifier les détails de la personne dans la réponse
        Map<String, Object> expectedDetails = new HashMap<>();
        expectedDetails.put("name", "John Doe");
        expectedDetails.put("address", "123 Main St");
        expectedDetails.put("email", "john@example.com");
        expectedDetails.put("phone", "1234567890");
        expectedDetails.put("age", 32);
        expectedDetails.put("medications", "Medicine1, Medicine2");
        expectedDetails.put("allergies", "Pollen");

        assertEquals(expectedDetails, responseEntity.getBody());
    }
}
