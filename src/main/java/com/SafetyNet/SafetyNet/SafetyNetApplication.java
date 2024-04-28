package com.SafetyNet.SafetyNet;

import com.SafetyNet.SafetyNet.controller.FireStationController;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import com.SafetyNet.SafetyNet.model.FireStation;
import com.SafetyNet.SafetyNet.model.MedicalRecord;
import com.SafetyNet.SafetyNet.model.Person;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import com.SafetyNet.SafetyNet.service.FireStationService;
import com.SafetyNet.SafetyNet.service.MedicalRecordService;
import com.SafetyNet.SafetyNet.service.PersonService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@SpringBootApplication
@ComponentScan(basePackages = {"com/SafetyNet/SafetyNet/service", "com/SafetyNet/SafetyNet/model", "com/SafetyNet/SafetyNet/controller", "com/SafetyNet/SafetyNet/repository"})
public class SafetyNetApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(SafetyNetApplication.class, args);
	}

	private static final Logger logger = LogManager.getLogger(SafetyNetApplication.class);


	private final PersonService personService;
	private final MedicalRecordService medicalRecordService;
	private final FireStationService fireStationService;
	private final FireStationController fireStationController;


	public SafetyNetApplication(PersonService personService, MedicalRecordService medicalRecordService, FireStationService fireStationService, FireStationController fireStationController) {
		this.personService = personService;
		this.medicalRecordService = medicalRecordService;
		this.fireStationService = fireStationService;
		this.fireStationController = fireStationController;

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
		logger.info("Début du chargement des données...");

		Jsonb jsonb = JsonbBuilder.newBuilder().build();

		ClassPathResource resource = new ClassPathResource("data/safety-net-data.json");

		String jsonData = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()), StandardCharsets.UTF_8);

		Map<String, List<Map<String, Object>>> data = jsonb.fromJson(jsonData, new HashMap<>() {}.getClass().getGenericSuperclass());

		loadMedicalRecords(data.get("medicalrecords"));
		loadFireStations(data.get("firestations"));
		loadPersons(data.get("persons"));

		logger.info("Chargement des données terminé.");
	}

	private void loadMedicalRecords(List<Map<String, Object>> medicalRecordsData) {
		logger.info("Début du chargement des dossiers médicaux...");

		medicalRecordsData.forEach(recordData -> {
			MedicalRecord medicalRecord = convertToMedicalRecord(recordData);
			medicalRecordService.addMedicalRecord(medicalRecord);
		});
		logger.info("Chargement des dossiers médicaux terminé.");
	}

	private void loadFireStations(List<Map<String, Object>> fireStationsData) {
		logger.info("Début du chargement des dossiers médicaux...");

		fireStationsData.forEach(stationData -> {
			FireStation fireStation = convertToFireStation(stationData);
			fireStationService.addMapping(fireStation);
		});
		logger.info("Chargement des casernes de pompiers terminé.");
	}

	private void loadPersons(List<Map<String, Object>> personsData) {
		logger.info("Début du chargement des dossiers médicaux...");
		personsData.forEach(personData -> {
			Person person = convertToPerson(personData);
			personService.addPerson(person);
		});
		logger.info("Chargement des personnes terminé.");
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
		String firstName = (String) data.get("firstName");
		String lastName = (String) data.get("lastName");
		String address = (String) data.get("address");
		String city = (String) data.get("city");
		String zip = (String) data.get("zip");
		String phone = (String) data.get("phone");
		String email = (String) data.get("email");

		return new Person(firstName, lastName, address, city, zip, phone, email);
	}

}
