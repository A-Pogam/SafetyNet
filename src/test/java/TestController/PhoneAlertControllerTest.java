package TestController;

import controller.PhoneAlertController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import service.FireStationService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class PhoneAlertControllerTest {

    @Mock
    private FireStationService fireStationService;

    @InjectMocks
    private PhoneAlertController phoneAlertController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetPhoneAlert_WithPhoneNumbers() {
        // Fire stations list
        List<Integer> fireStations = new ArrayList<>();
        fireStations.add(1);
        fireStations.add(2);

        // Mock phone numbers
        List<String> phoneNumbers = new ArrayList<>();
        phoneNumbers.add("1234567890");
        phoneNumbers.add("9876543210");

        // Mock service behavior
        when(fireStationService.getPhoneNumbersServedByFireStations(fireStations)).thenReturn(phoneNumbers);

        // Call the method under test
        ResponseEntity<List<String>> responseEntity = phoneAlertController.getPhoneAlert(fireStations);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(phoneNumbers, responseEntity.getBody());
    }

    @Test
    public void testGetPhoneAlert_NoPhoneNumbers() {
        // Fire stations list
        List<Integer> fireStations = new ArrayList<>();
        fireStations.add(1);
        fireStations.add(2);

        // Mock service behavior
        when(fireStationService.getPhoneNumbersServedByFireStations(fireStations)).thenReturn(new ArrayList<>());

        // Call the method under test
        ResponseEntity<List<String>> responseEntity = phoneAlertController.getPhoneAlert(fireStations);

        // Verify the response
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() == null || responseEntity.getBody().isEmpty()); // Check if the body is null or empty
    }

}

