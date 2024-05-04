package TestController;

import com.SafetyNet.SafetyNet.SafetyNetApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.SafetyNet.SafetyNet.controller.PersonInfoController;
import com.SafetyNet.SafetyNet.model.MedicalRecord;
import com.SafetyNet.SafetyNet.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import com.SafetyNet.SafetyNet.service.FireStationService;
import com.SafetyNet.SafetyNet.service.MedicalRecordService;
import com.SafetyNet.SafetyNet.service.PersonService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = SafetyNetApplication.class)
@AutoConfigureMockMvc


public class PersonInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PersonService personService;

    @Mock
    private MedicalRecordService medicalRecordService;

    @Mock
    private FireStationService fireStationService;

    @Autowired
    private ObjectMapper objectMapper;

    private PersonInfoController personInfoController;

    @BeforeEach
    void setUp() {
        personInfoController = new PersonInfoController(personService, medicalRecordService, fireStationService);
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
        ResponseEntity<?> responseEntity = personInfoController.getPersonInfo("John", "Doe");

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