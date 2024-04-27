package TestController;

import com.SafetyNet.SafetyNet.controller.ChildAlertController;
import com.SafetyNet.SafetyNet.dto.ChildInfo;
import com.SafetyNet.SafetyNet.model.MedicalRecord;
import com.SafetyNet.SafetyNet.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.SafetyNet.SafetyNet.service.FireStationService;
import com.SafetyNet.SafetyNet.service.MedicalRecordService;
import com.SafetyNet.SafetyNet.service.PersonService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ChildAlertControllerTest {

    @Mock
    private PersonService personService;

    @Mock
    private FireStationService fireStationService;

    @Mock
    private MedicalRecordService medicalRecordService;

    @InjectMocks
    private ChildAlertController childAlertController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testMapPersonToChildInfo_ValidPerson() {
        // Créer un objet Person simulé
        Person person = new Person("John", "Doe", "123 Main St", "Anytown", "12345", "555-555-5555", "john@example.com");

        // Créer un objet MedicalRecord simulé
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setBirthdate("01/01/2005"); // Age = 17 ans (en 2022)

        // Définir le comportement des services mockés
        when(medicalRecordService.getMedicalRecordByName("John", "Doe")).thenReturn(medicalRecord);
        when(fireStationService.calculateAge("01/01/2005")).thenReturn(17);

        // Appeler la méthode à tester
        ChildInfo childInfo = childAlertController.
                mapPersonToChildInfo(person);

        // Vérifier si les détails de l'enfant sont corrects
        assertEquals("John", childInfo.getFirstName());
        assertEquals("Doe", childInfo.getLastName());
        assertEquals(17, childInfo.getAge());
    }

    @Test
    public void testMapPersonToChildInfo_NullMedicalRecord() {
        // Créer un objet Person simulé
        Person person = new Person("John", "Doe", "123 Main St", "Anytown", "12345", "555-555-5555", "john@example.com");

        // Définir le comportement des services mockés
        when(medicalRecordService.getMedicalRecordByName("John", "Doe")).thenReturn(null);

        // Appeler la méthode à tester
        ChildInfo childInfo = childAlertController.mapPersonToChildInfo(person);

        // Vérifier si la méthode renvoie null
        assertEquals(null, childInfo);
    }

    @Test
    public void testGetChildAlert() {
        // Adresse simulée
        String address = "123 Main St";

        // Créer une liste simulée de personnes (avec au moins un enfant)
        List<Person> residents = new ArrayList<>();
        residents.add(new Person("John", "Doe", address, "Anytown", "12345", "555-555-5555", "john@example.com"));
        residents.add(new Person("Jane", "Doe", address, "Anytown", "12345", "555-555-5555", "jane@example.com"));

        // Créer un objet MedicalRecord simulé
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setBirthdate("01/01/2005"); // Age = 17 ans (en 2022)

        // Définir le comportement des services mockés
        when(personService.getPersonsByAddress(address)).thenReturn(residents);
        when(medicalRecordService.getMedicalRecordByName("John", "Doe")).thenReturn(medicalRecord);
        when(fireStationService.calculateAge("01/01/2005")).thenReturn(17);

        // Appeler la méthode à tester
        List<ChildInfo> childAlert = childAlertController.getChildAlert(address).getBody();

        // Vérifier si la liste des enfants contient les informations attendues
        assertEquals(1, childAlert.size());
        assertEquals("John", childAlert.get(0).getFirstName());
        assertEquals("Doe", childAlert.get(0).getLastName());
        assertEquals(17, childAlert.get(0).getAge());
    }
}

