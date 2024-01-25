package com.SafetyNet.SafetyNet;

import model.FireStation;
import model.MedicalRecord;
import service.FireStationService;
import service.MedicalRecordService;
import service.PersonService;
import jakarta.annotation.PostConstruct;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;


import model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@EntityScan(basePackages = "model")
@EnableJpaRepositories(basePackages = "repository")
@ComponentScan(basePackages = {"service"})
public class SafetyNetApplication {

	@Autowired
	private PersonService personService;

	@Autowired
	private MedicalRecordService medicalRecordService;

	@Autowired
	private FireStationService fireStationService;

	public static void main(String[] args) {
		SpringApplication.run(SafetyNetApplication.class, args);
	}

	@PostConstruct
	public void loadData() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		InputStream inputStream = getClass().getResourceAsStream("/data/safety-net-data.json");
		Map<String, List<Map<String, Object>>> data = objectMapper.readValue(inputStream, new TypeReference<>() {
		});

		// Load MedicalRecord data
		List<Map<String, Object>> medicalRecordsData = data.get("medicalrecords");
		if (medicalRecordsData != null) {
			for (Map<String, Object> medicalRecordData : medicalRecordsData) {
				MedicalRecord medicalRecord = convertToMedicalRecord(medicalRecordData);
				medicalRecordService.addMedicalRecord(medicalRecord);
			}
		}

		// Load FireStation data
		List<Map<String, Object>> fireStationsData = data.get("firestations");
		if (fireStationsData != null) {
			for (Map<String, Object> fireStationData : fireStationsData) {
				FireStation fireStation = convertToFireStation(fireStationData);
				fireStationService.addMapping(fireStation);
			}
		}

		// Load Person data
		List<Map<String, Object>> personsData = data.get("persons");
		if (personsData != null) {
			for (Map<String, Object> personData : personsData) {
				Person person = convertToPerson(personData);
				personService.addPerson(person);
			}
		}
	}

	private MedicalRecord convertToMedicalRecord(Map<String, Object> data) {
		MedicalRecord medicalRecord = new MedicalRecord();
		medicalRecord.setFirstName((String) data.get("firstName"));
		medicalRecord.setLastName((String) data.get("lastName"));
		medicalRecord.setBirthdate((String) data.get("birthdate"));

		ObjectMapper objectMapper = new ObjectMapper();

		// using ObjectMapper to convert to List<String>
		List<String> medications = objectMapper.convertValue(data.get("medications"), new TypeReference<>() {});
		List<String> allergies = objectMapper.convertValue(data.get("allergies"), new TypeReference<>() {});

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




}
