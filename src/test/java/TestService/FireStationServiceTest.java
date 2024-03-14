package TestService;

import dto.CoveredPerson;
import dto.FireStationCoverage;
import model.FireStation;
import model.MedicalRecord;
import model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.FireStationService;
import service.PersonService;
import service.MedicalRecordService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FireStationServiceTest {
    @Mock
    private List<FireStation> fireStations;

    @Mock
    private PersonService personService;

    @Mock
    private MedicalRecordService medicalRecordService;

    @InjectMocks
    private FireStationService fireStationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        // Configuration des données de test
        List<Person> allPersons = new ArrayList<>();
        allPersons.add(new Person("John", "Doe", "123 Main St", "Légume", "12", "1234", "john@example.com"));
        allPersons.add(new Person("Jane", "Doe", "123 Main St", "Légume", "12", "1235", "jane@example.com"));

        // Stubbing pour la méthode getAllPersons() de personService
        when(personService.getAllPersons()).thenReturn(allPersons);
    }

    @Test
    void testGetPhoneNumbersServedByFireStations() {
        // Données de test
        List<Integer> firestations = Arrays.asList(1, 2);

        // Stubbing pour la méthode getPersonsByAddress() de personService
        // Stubbing pour la méthode getPersonsByAddress() de personService
        when(personService.getPersonsByAddress("123 Main St")).thenReturn(Arrays.asList(
                new Person("John", "Doe", "123 Main St", "Légume", "12", "1234", "john@example.com"),
                new Person("Jane", "Doe", "123 Main St", "Légume", "12", "1235", "jane@example.com")
        ));


        // Stubbing pour la méthode getMedicalRecordByName() de medicalRecordService
        when(medicalRecordService.getMedicalRecordByName(anyString(), anyString())).thenReturn(new MedicalRecord());

        // Appel de la méthode à tester
        List<String> result = fireStationService.getPhoneNumbersServedByFireStations(firestations);

        // Vérification du résultat
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("1234", result.get(0));
        assertEquals("1235", result.get(1));
    }



    @Test
    void testUpdateFireStationNumber() {
        // Données de test
        String address = "123 Main St";
        int stationNumber = 3;
        FireStation fireStation = new FireStation(address, 1);

        // Assurez-vous que fireStations n'est pas null et contient la fireStation de test
        assertNotNull(fireStations);
        fireStations.add(fireStation);

        // Stubbing pour la méthode getAllFireStations() de fireStationService
        when(fireStationService.getAllFireStations()).thenReturn(fireStations);

        // Appel de la méthode à tester
        FireStation result = fireStationService.updateFireStationNumber(address, stationNumber);

        // Vérification du résultat
        assertNotNull(result);
        assertEquals(stationNumber, result.getStation());
    }





    @Test
    void testDeleteMapping() {
        // Données de test
        String address = "123 Main St";
        FireStation fireStation = new FireStation(address, 1);

        // Ajoutez la station d'incendie à la liste
        fireStations.add(fireStation);

        // Appel de la méthode à tester
        boolean result = fireStationService.deleteMapping(address);

        // Vérification du résultat
        assertTrue(result);
        verify(fireStations, times(1)).removeIf(existingMapping -> existingMapping.getAddress().equals(address));
    }

    @Test
    void testGetFireStationNumberByAddress() {
        // Données de test
        String address = "123 Main St";
        int stationNumber = 3;
        FireStation fireStation = new FireStation(address, stationNumber);

        // Assurez-vous que fireStations n'est pas null et contient la fireStation de test
        assertNotNull(fireStations);
        when(fireStationService.getAllFireStations()).thenReturn(fireStations);
        when(fireStations.stream()).thenReturn(Stream.of(fireStation)); // Stubbing pour la méthode stream()

        // Appel de la méthode à tester
        Integer result = fireStationService.getFireStationNumberByAddress(address);

        // Vérification du résultat
        assertNotNull(result);
        assertEquals(stationNumber, result.intValue());
    }


}
