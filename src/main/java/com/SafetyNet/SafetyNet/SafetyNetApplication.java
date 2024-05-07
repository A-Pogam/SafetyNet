package com.SafetyNet.SafetyNet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import com.SafetyNet.SafetyNet.model.FireStation;
import com.SafetyNet.SafetyNet.model.MedicalRecord;
import com.SafetyNet.SafetyNet.model.Person;
import com.SafetyNet.SafetyNet.repository.contracts.IFireStationRepository;
import com.SafetyNet.SafetyNet.repository.contracts.IMedicalRecordRepository;
import com.SafetyNet.SafetyNet.repository.contracts.IPersonRepository;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

@SpringBootApplication
@ComponentScan
public class SafetyNetApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(SafetyNetApplication.class, args);
	}

	private static final Logger logger = LogManager.getLogger(SafetyNetApplication.class);

	@Autowired
	private IPersonRepository iPersonRepository;
	@Autowired
	private IMedicalRecordRepository iMedicalRecordRepository;
	@Autowired
	private IFireStationRepository iFireStationRepository;

	@Override
	public void run(String... args) {
		try {
			loadData();
		} catch (Exception e) {
			logger.error("Error while loading data.", e);
		}
	}

	private void loadData() throws IOException {
		logger.debug("Start loading data...");

		Jsonb jsonb = JsonbBuilder.newBuilder().build();

		ClassPathResource resource = new ClassPathResource("data/safety-net-data.json");

		String jsonData = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()), StandardCharsets.UTF_8);

		Map<String, List<Map<String, Object>>> data = jsonb.fromJson(jsonData, new HashMap<>() {
			private static final long serialVersionUID = 3736509733419015752L;
		}.getClass().getGenericSuperclass());

		loadMedicalRecords(data.get("medicalrecords"));
		loadFireStations(data.get("firestations"));
		loadPersons(data.get("persons"));

		logger.debug("Data loading complete.");
	}

	private void loadMedicalRecords(List<Map<String, Object>> medicalRecordsData) {
		logger.debug("Start loading medical records...");

		medicalRecordsData.forEach(recordData -> {
			MedicalRecord medicalRecord;
			try {
				medicalRecord = convertToMedicalRecord(recordData);
				iMedicalRecordRepository.save(medicalRecord);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		logger.debug("Medical records loading complete.");
	}

	private void loadFireStations(List<Map<String, Object>> fireStationsData) {
		logger.debug("Start loading of fire stations...");

		fireStationsData.forEach(stationData -> {
			FireStation fireStation = convertToFireStation(stationData);
			iFireStationRepository.save(fireStation);
		});
		logger.debug("Fire stations loading complete.");
	}

	private void loadPersons(List<Map<String, Object>> personsData) {
		logger.debug("Start loading people...");
		personsData.forEach(personData -> {
			Person person = convertToPerson(personData);
			iPersonRepository.save(person);
		});
		logger.debug("Persons loading complete.");
	}

	private MedicalRecord convertToMedicalRecord(Map<String, Object> data) throws Exception {
		MedicalRecord medicalRecord = new MedicalRecord();
		medicalRecord.setFirstName((String) data.get("firstName"));
		medicalRecord.setLastName((String) data.get("lastName"));

		String birthdateAsString = (String) data.get("birthdate");
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		LocalDate birthDate = sdf.parse(birthdateAsString).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		medicalRecord.setBirthdate(birthDate);

		Jsonb jsonb = JsonbBuilder.newBuilder().build();

		List<String> medications = jsonb.fromJson(jsonb.toJson(data.get("medications")), new ArrayList<>() {
			private static final long serialVersionUID = 7188850748491737504L;
		}.getClass().getGenericSuperclass());
		List<String> allergies = jsonb.fromJson(jsonb.toJson(data.get("allergies")), new ArrayList<>() {
			private static final long serialVersionUID = -4937781902076209666L;
		}.getClass().getGenericSuperclass());
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