package com.SafetyNet.SafetyNet;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import model.FireStation;
import model.MedicalRecord;
import service.FireStationService;
import service.MedicalRecordService;
import service.PersonService;
import jakarta.annotation.PostConstruct;

import model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication(scanBasePackages = {"controller","service","model", "repository"})
public class SafetyNetApplication {

	private final PersonService personService;
	private final MedicalRecordService medicalRecordService;
	private final FireStationService fireStationService;

	@Autowired
	public SafetyNetApplication(PersonService personService, MedicalRecordService medicalRecordService, FireStationService fireStationService) {
		this.personService = personService;
		this.medicalRecordService = medicalRecordService;
		this.fireStationService = fireStationService;
	}


	public static void main(String[] args) {
		SpringApplication.run(SafetyNetApplication.class, args);
	}

	@PostConstruct
	public void loadData() throws IOException {
		Jsonb jsonb = JsonbBuilder.newBuilder().build();
		InputStream inputStream = getClass().getResourceAsStream("/data/safety-net-data.json");
		Map<String, List<Map<String, Object>>> data = jsonb.fromJson(inputStream, new HashMap<String, List<Map<String, Object>>>(){}.getClass().getGenericSuperclass());

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

		Jsonb jsonb = JsonbBuilder.newBuilder().build();

		// Deserialize medications and allergies directly from JSON
		List<String> medications = jsonb.fromJson(jsonb.toJson(data.get("medications")), new ArrayList<String>(){}.getClass().getGenericSuperclass());
		List<String> allergies = jsonb.fromJson(jsonb.toJson(data.get("allergies")), new ArrayList<String>(){}.getClass().getGenericSuperclass());



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
