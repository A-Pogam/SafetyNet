package com.SafetyNet.SafetyNet.TestService;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.SafetyNet.SafetyNet.dto.FireStationCoverage;
import com.SafetyNet.SafetyNet.model.FireStation;
import com.SafetyNet.SafetyNet.service.contracts.IFireStationService;

@SpringBootTest
@AutoConfigureMockMvc
public class FireStationServiceIT {

    @Autowired
    private IFireStationService iFireStationService;

    @Test
    public void getAllFireStations_returnFireStations () {
        List<FireStation> fireStations = iFireStationService.getAllFireStations();

        assertThat(fireStations).isNotEmpty();
        assertThat(fireStations.getFirst().getAddress()).isEqualTo("1509 Culver St");
        assertThat(fireStations.getFirst().getStation()).isEqualTo(3);
    }

    @Test
    public void addMapping_returnFireStationMapping() {
        FireStation newMapping = new FireStation("1A, Common St", 7);

        FireStation mappingAdded = iFireStationService.addMapping(newMapping);

        assertThat(mappingAdded.getAddress()).isEqualTo(newMapping.getAddress());
        assertThat(mappingAdded.getStation()).isEqualTo(newMapping.getStation());
    }

    @Test
    public void addMapping_returnNull() {
        FireStation newMapping = new FireStation("951 LoneTree Rd", 2);

        FireStation mappingAdded = iFireStationService.addMapping(newMapping);

        assertThat(mappingAdded).isNull();
    }

    @Test
    public void updateFireStationNumber_returnFireStationMapping() {
        FireStation mappingUpdated = iFireStationService.updateFireStationNumber("908 73rd St", 1, 7);

        assertThat(mappingUpdated.getAddress()).isEqualTo("908 73rd St");
        assertThat(mappingUpdated.getStation()).isEqualTo(7);
    }

    @Test
    public void updateFireStationNumber_returnNull() {
        FireStation mappingUpdated = iFireStationService.updateFireStationNumber("2B Unknown Rd", 42, 7);
        assertThat(mappingUpdated).isNull();
    }

    @Test
    public void deleteMapping_returnTrue() {
        boolean isDeleted = iFireStationService.deleteMapping("29 15th St", 2);
        assertThat(isDeleted).isTrue();
    }

    @Test
    public void deleteMapping_returnFalse() {
        boolean isDeleted = iFireStationService.deleteMapping("2B Unknown Rd", 42);
        assertThat(isDeleted).isFalse();
    }

    @Test
    public void getCoverageByStationNumber_returnFireStationCoverage() {
        FireStationCoverage fireStationCoverage = iFireStationService.getCoverageByStationNumber(3);

        assertThat(fireStationCoverage.getAdultCount()).isEqualTo(10);
        assertThat(fireStationCoverage.getChildrenCount()).isEqualTo(3);
        assertThat(fireStationCoverage.getCoveragePeople()).isNotEmpty();
        assertThat(fireStationCoverage.getCoveragePeople().getFirst().getPerson().getFirstname()).isEqualTo("John");
        assertThat(fireStationCoverage.getCoveragePeople().getFirst().getPerson().getLastname()).isEqualTo("Boyd");
        assertThat(fireStationCoverage.getCoveragePeople().getFirst().getPerson().getAddress()).isEqualTo("1509 Culver St");
        assertThat(fireStationCoverage.getCoveragePeople().getFirst().getPerson().getCity()).isEqualTo("Culver");
        assertThat(fireStationCoverage.getCoveragePeople().getFirst().getPerson().getZip()).isEqualTo("97451");
        assertThat(fireStationCoverage.getCoveragePeople().getFirst().getPerson().getPhone()).isEqualTo("841-874-6512");
        assertThat(fireStationCoverage.getCoveragePeople().getFirst().getPerson().getEmail()).isEqualTo("jaboyd@email.com");
        assertThat(fireStationCoverage.getCoveragePeople().getFirst().getAge()).isPositive();
        assertThat(fireStationCoverage.getCoveragePeople().getLast().getPerson().getFirstname()).isEqualTo("Clive");
        assertThat(fireStationCoverage.getCoveragePeople().getLast().getPerson().getLastname()).isEqualTo("Ferguson");
    }

    @Test
    public void getResidentsAndFireStationByAddress_returnResponse() {
        Map<String, Object> firstResident = new HashMap<String, Object>();
        firstResident.put("firstName", "Tony");
        firstResident.put("lastName", "Cooper");
        firstResident.put("age", 30);
        firstResident.put("phone", "841-874-6874");
        firstResident.put("medications", new ArrayList<>(Arrays.asList("hydrapermazol:300mg", "dodoxadin:30mg")));
        firstResident.put("allergies", new ArrayList<>(Arrays.asList("shellfish")));

        Map<String, Object> response = iFireStationService.getResidentsAndFireStationByAddress("112 Steppes Pl");

        assertThat(response).extracting("residents", as(InstanceOfAssertFactories.LIST)).contains(firstResident);
        assertThat(response.get("fireStationNumbers")).isEqualTo(new ArrayList<>(Arrays.asList(3, 4)));
    }

    @Test
    public void getResidentsAndFireStationByAddress_returnNull() {
        Map<String, Object> response = iFireStationService.getResidentsAndFireStationByAddress("2B Unknown Rd");
        assertThat(response).isNull();
    }

    @Test
    public void getFloodStations_returnFloodStationsDetails() {
        Map<String, Object> firstResident = new HashMap<String, Object>();
        firstResident.put("name", "John Boyd");
        firstResident.put("phone", "841-874-6512");
        firstResident.put("age", 40);
        firstResident.put("medications", new ArrayList<>(Arrays.asList("aznol:350mg", "hydrapermazol:100mg")));
        firstResident.put("allergies", new ArrayList<>(Arrays.asList("nillacilan")));

        List<Map<String, Object>> floodStationsDetails = iFireStationService.getFloodStations(new ArrayList<>(Arrays.asList(3)));

        assertThat(floodStationsDetails.getFirst().get("address")).isEqualTo("1509 Culver St");
        assertThat(floodStationsDetails.getFirst()).extracting("residents", as(InstanceOfAssertFactories.LIST)).contains(firstResident);
    }

    @Test
    public void getPhoneNumbersServedByFireStation_returnPhoneNumbers() {
        List<String> phoneNumbers = iFireStationService.getPhoneNumbersServedByFireStation(3);
        assertThat(phoneNumbers).isNotEmpty();
    }
}