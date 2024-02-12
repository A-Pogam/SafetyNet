package com.SafetyNet.SafetyNet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import model.FireStation;
import model.MedicalRecord;
import model.Person;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import service.FireStationService;
import service.MedicalRecordService;
import service.PersonService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Component
@SpringBootApplication
@ComponentScan(basePackages = {"service", "model", "controller", "repository"})
public class SafetyNetApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(SafetyNetApplication.class, args);
	}

	private static final Logger logger = LogManager.getLogger(SafetyNetApplication.class);


	private final PersonService personService;
	private final MedicalRecordService medicalRecordService;
	private final FireStationService fireStationService;

	public SafetyNetApplication(PersonService personService, MedicalRecordService medicalRecordService, FireStationService fireStationService) {
		this.personService = personService;
		this.medicalRecordService = medicalRecordService;
		this.fireStationService = fireStationService;
	}

	@Override
	public void run(String... args) {
		try {
			loadData();
		} catch (Exception e) {
			logger.error("Erreur lors du chargement des données.", e);
		}
	}

	private void loadData() throws IOException {
		Jsonb jsonb = JsonbBuilder.newBuilder().build();

		ClassPathResource resource = new ClassPathResource("/data/safety-net-data.json");

		String jsonData = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()), StandardCharsets.UTF_8);

		Map<String, List<Map<String, Object>>> data = jsonb.fromJson(jsonData, new HashMap<>() {}.getClass().getGenericSuperclass());

		loadMedicalRecords(data.get("medicalrecords"));
		loadFireStations(data.get("firestations"));
		loadPersons(data.get("persons"));
	}

	private void loadMedicalRecords(List<Map<String, Object>> medicalRecordsData) {
		medicalRecordsData.forEach(recordData -> {
			MedicalRecord medicalRecord = convertToMedicalRecord(recordData);
			medicalRecordService.addMedicalRecord(medicalRecord);
		});
	}

	private void loadFireStations(List<Map<String, Object>> fireStationsData) {
		fireStationsData.forEach(stationData -> {
			FireStation fireStation = convertToFireStation(stationData);
			fireStationService.addMapping(fireStation);
		});
	}

	private void loadPersons(List<Map<String, Object>> personsData) {
		personsData.forEach(personData -> {
			Person person = convertToPerson(personData);
			personService.addPerson(person);
		});
	}

	private MedicalRecord convertToMedicalRecord(Map<String, Object> data) {
		MedicalRecord medicalRecord = new MedicalRecord();
		medicalRecord.setFirstName((String) data.get("firstName"));
		medicalRecord.setLastName((String) data.get("lastName"));
		medicalRecord.setBirthdate((String) data.get("birthdate"));

		Jsonb jsonb = JsonbBuilder.newBuilder().build();

		List<String> medications = jsonb.fromJson(jsonb.toJson(data.get("medications")), new ArrayList<>() {}.getClass().getGenericSuperclass());
		List<String> allergies = jsonb.fromJson(jsonb.toJson(data.get("allergies")), new ArrayList<>() {}.getClass().getGenericSuperclass());

		medicalRecord.setMedications(medications != null ? medications : new ArrayList<>());
		medicalRecord.setAllergies(allergies != null ? allergies : new ArrayList<>());

		return medicalRecord;
	}

	private FireStation convertToFireStation(Map<String, Object> data) {
		FireStation fireStation = new FireStation();
		fireStation.setAddress((String) data.get("address"));
		fireStation.setStation(Integer.parseInt(String.valueOf(data.get("station"))));
		return fireStation;
	}

	private Person convertToPerson(Map<String, Object> data) {
		Person person = new Person();
		person.setFirstname((String) data.get("firstName"));
		person.setLastname((String) data.get("lastName"));
		person.setAddress((String) data.get("address"));
		person.setCity((String) data.get("city"));
		person.setZip((String) data.get("zip"));
		person.setPhone((String) data.get("phone"));
		person.setEmail((String) data.get("email"));
		return person;
	}
	@Configuration
	public class JacksonConfiguration {

		@Bean
		public ObjectMapper objectMapper() {
			return new ObjectMapper();
		}
	}

}
