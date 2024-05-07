package TestController;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;

import com.SafetyNet.SafetyNet.controller.FireStationController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.SafetyNet.SafetyNet.dto.CoveredPerson;
import com.SafetyNet.SafetyNet.dto.FireStationCoverage;
import com.SafetyNet.SafetyNet.model.Person;
import com.SafetyNet.SafetyNet.service.contracts.IFireStationService;

@WebMvcTest(controllers = FireStationController.class)
public class FireStationCoverageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IFireStationService iFireStationService;

    private Person firstPerson = new Person("Geralt", "De Riv", "Kaer Morhen", "Kaer Morhen", "12345", "0102030405", "geralt.deriv@kaermorhen.kdw");
    private Person secondPerson = new Person("Cirilla", "Fiona Ellen Rianon", "Cintra", "Cintra", "54321", "0504030201", "ciri@kaermorhen.kdw");

    private CoveredPerson firstCoveredPerson = new CoveredPerson(firstPerson, 94);
    private CoveredPerson secondCoverdPerson = new CoveredPerson(secondPerson, 14);

    @Test
    public void getFireStationCoverage_returnOk() throws Exception {
        FireStationCoverage fireStationCoverage = new FireStationCoverage(new ArrayList<>(Arrays.asList(firstCoveredPerson, secondCoverdPerson)), 1, 1);

        when(iFireStationService.getCoverageByStationNumber(anyInt()))
                .thenReturn(fireStationCoverage);

        mockMvc.perform(get("/firestation")
                        .param("stationNumber", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.coveragePeople.[0].person.firstname").value("Geralt"))
                .andExpect(jsonPath("$.coveragePeople.[0].person.lastname").value("De Riv"))
                .andExpect(jsonPath("$.coveragePeople.[0].person.address").value("Kaer Morhen"))
                .andExpect(jsonPath("$.coveragePeople.[0].person.city").value("Kaer Morhen"))
                .andExpect(jsonPath("$.coveragePeople.[0].person.zip").value("12345"))
                .andExpect(jsonPath("$.coveragePeople.[0].person.phone").value("0102030405"))
                .andExpect(jsonPath("$.coveragePeople.[0].person.email").value("geralt.deriv@kaermorhen.kdw"))
                .andExpect(jsonPath("$.coveragePeople.[0].age").value(94))
                .andExpect(jsonPath("$.coveragePeople.[1].person").exists())
                .andExpect(jsonPath("$.coveragePeople.[1].age").value(14))
                .andExpect(jsonPath("$.adultCount").value(1))
                .andExpect(jsonPath("$.childrenCount").value(1));
    }

    @Test
    public void getFireStationCoverage_returnNotFound() throws Exception {
        when(iFireStationService.getCoverageByStationNumber(anyInt()))
                .thenReturn(new FireStationCoverage(new ArrayList<>(), 0, 0));

        mockMvc.perform(get("/firestation")
                        .param("stationNumber", "7")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

