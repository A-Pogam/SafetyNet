package com.SafetyNet.SafetyNet;

import Service.FireStationService;
import Service.MedicalRecordService;
import Service.PersonService;
import jakarta.annotation.PostConstruct;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;



import model.MedicalRecord;
import model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SpringBootApplication
@EntityScan(basePackages = "com.SafetyNet.SafetyNet.model")
@EnableJpaRepositories(basePackages = "com.SafetyNet.SafetyNet.repository")
@ComponentScan(basePackages = {"com.SafetyNet.SafetyNet.service"})
public class SafetyNetApplication {

	@Autowired //inject dependency
	private PersonService personService;

	@Autowired
	private MedicalRecordService medicalRecordService;

	@Autowired
	private FireStationService fireStationService;

	public static void main(String[] args) {
		SpringApplication.run(SafetyNetApplication.class, args);
	}

	@PostConstruct //method loaddata will be called after creation instance safetynetApplication, used by Spring when app start
	public void loadData() throws IOException { //read JSON file and convert (list, savelall, personservice)
		ObjectMapper objectMapper = new ObjectMapper();
		InputStream inputStream = getClass().getResourceAsStream("/data/safety-net-data.json");
		List<Person> persons = objectMapper.readValue(inputStream, new TypeReference<>() {});

		personService.saveAll(persons);

	}
}
