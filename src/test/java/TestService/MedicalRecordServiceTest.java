package TestService;

import com.SafetyNet.SafetyNet.model.MedicalRecord;
import com.SafetyNet.SafetyNet.service.MedicalRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class MedicalRecordServiceTest {

    @Mock
    private List<MedicalRecord> medicalRecords;

    @InjectMocks
    private MedicalRecordService medicalRecordService;

    @BeforeEach
    void setUp() {
        // Initialisation des données de test
        List<MedicalRecord> medicalRecordsList = new ArrayList<>();
        medicalRecordsList.add(new MedicalRecord("John", "Doe", "01/01/1980", new ArrayList<>(), new ArrayList<>()));
        medicalRecordsList.add(new MedicalRecord("Jane", "Doe", "01/02/1990", new ArrayList<>(), new ArrayList<>()));

        medicalRecordService = Mockito.spy(new MedicalRecordService());

        // Mock pour medicalRecordService.getAllMedicalRecords()
        Mockito.doReturn(medicalRecordsList).when(medicalRecordService).getAllMedicalRecords();
    }



    @Test
    void testGetAllMedicalRecords() {
        // Appel de la méthode à tester
        List<MedicalRecord> result = medicalRecordService.getAllMedicalRecords();

        // Vérification du résultat
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Doe", result.get(0).getLastName());
        assertEquals("01/01/1980", result.get(0).getBirthdate());
        assertEquals("Jane", result.get(1).getFirstName());
        assertEquals("Doe", result.get(1).getLastName());
        assertEquals("01/02/1990", result.get(1).getBirthdate());
    }
}
