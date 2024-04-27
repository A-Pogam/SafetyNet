package TestService;

import com.SafetyNet.SafetyNet.model.FireStation;
import com.SafetyNet.SafetyNet.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.SafetyNet.SafetyNet.service.FireStationService;
import com.SafetyNet.SafetyNet.service.PersonService;
import com.SafetyNet.SafetyNet.service.MedicalRecordService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FireStationServiceTest {

    private List<FireStation> fireStations;
    private PersonService personService;
    private List<Person> persons;
    private MedicalRecordService medicalRecordService;
    private FireStationService fireStationService;

    @BeforeEach
    void setUp() {
        fireStations = new ArrayList<>();
        personService = mock(PersonService.class);
        persons = new ArrayList<>();
        medicalRecordService = mock(MedicalRecordService.class);
        fireStationService = new FireStationService(fireStations, personService, persons, medicalRecordService);
    }

    @Test
    void testUpdateFireStationNumber() {
        // Given
        String address = "123 Main St";
        int originalStationNumber = 1;
        int updatedStationNumber = 2;
        FireStation fireStation = new FireStation(address, originalStationNumber);
        fireStations.add(fireStation);

        // When
        fireStationService.updateFireStationNumber(address, updatedStationNumber);

        // Then
        assertEquals(updatedStationNumber, fireStation.getStation());
    }

    @Test
    void testGetFireStationNumberByAddress() {
        // Given
        String address = "123 Main St";
        int stationNumber = 1;
        fireStations.add(new FireStation(address, stationNumber));

        // When
        Integer retrievedStationNumber = fireStationService.getFireStationNumberByAddress(address);

        // Then
        assertEquals(stationNumber, retrievedStationNumber);
    }

    @Test
    void testDeleteMapping() {
        // Given
        String address = "123 Main St";
        int stationNumber = 1;
        fireStations.add(new FireStation(address, stationNumber));

        // When
        boolean deleted = fireStationService.deleteMapping(address);

        // Then
        assertTrue(deleted);
        assertNull(fireStationService.getFireStationNumberByAddress(address));
    }
}

