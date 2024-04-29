package com.SafetyNet.SafetyNet.service;


import com.SafetyNet.SafetyNet.dto.ChildInfo;
import com.SafetyNet.SafetyNet.model.MedicalRecord;
import com.SafetyNet.SafetyNet.model.Person;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {
    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);


    private final List<Person> persons = new ArrayList<>();
    private final List<MedicalRecord> medicalRecords = new ArrayList<>();

    public List<Person> getAllPersons() {
        logger.info("Retrieving all persons");
        return new ArrayList<>(persons);
    }

    public Person addPerson(Person person) {
        logger.info("Adding person: {}", person);
        if (!personExists(person.getFirstname(), person.getLastname())) {
            persons.add(person);
            logger.info("Person added successfully");
            return person;
        } else {
            logger.warn("Person already exists: {}", person);
            return null;
        }
    }



    public boolean updatePerson(String firstName, String lastName, Person updatedPerson) {
        Person existingPerson = getPersonByName(firstName, lastName);
        if (existingPerson != null) {
            // Mettre à jour les informations de la personne
            existingPerson.setAddress(updatedPerson.getAddress());
            existingPerson.setCity(updatedPerson.getCity());
            existingPerson.setZip(updatedPerson.getZip());
            existingPerson.setPhone(updatedPerson.getPhone());
            existingPerson.setEmail(updatedPerson.getEmail());
            return true;
        } else {
            return false; // Personne non trouvée pour la mise à jour
        }
    }

    public boolean deletePerson(String firstName, String lastName) {
        logger.info("Deleting person with name: {}, {}", firstName, lastName);
        List<Person> matchingPersons = persons.stream()
                .filter(person -> person.getFirstname().equals(firstName) && person.getLastname().equals(lastName))
                .collect(Collectors.toList());

        if (!matchingPersons.isEmpty()) {
            persons.removeAll(matchingPersons);
            logger.info("Person(s) deleted successfully");
            return true;
        } else {
            logger.warn("No person found for deletion with name: {}, {}", firstName, lastName);
            return false;
        }
    }

    public boolean personExists(String firstName, String lastName) {
        return persons.stream()
                .anyMatch(person -> person.getFirstname().equals(firstName) && person.getLastname().equals(lastName));
    }


    public Person getPersonByName(String firstName, String lastName) {
        logger.info("Searching for person with name: {}, {}", firstName, lastName);
        return persons.stream()
                .filter(person -> person.getFirstname().equals(firstName) && person.getLastname().equals(lastName))
                .findFirst()
                .orElse(null);
    }


    public List<Person> getPersonsByAddress(String address) {
        logger.info("Retrieving persons by address: {}", address);
        return persons.stream()
                .filter(person -> person.getAddress().equals(address))
                .collect(Collectors.toList());
    }

    public List<String> getEmailsByCity(String city) {
        logger.info("Retrieving emails for persons in city: {}", city);
        return persons.stream()
                .filter(person -> person.getCity().equals(city))
                .map(Person::getEmail)
                .collect(Collectors.toList());
    }

    public ChildInfo mapPersonToChildInfo(Person person, FireStationService fireStationService, MedicalRecordService medicalRecordService) {
        // Get medical record for the person
        MedicalRecord medicalRecord = medicalRecordService.getMedicalRecordByName(person.getFirstname(), person.getLastname());
        if (medicalRecord == null) {
            return null;
        }

        // Calculate age based on birthdate using FireStationService
        int age = fireStationService.calculateAge(medicalRecord.getBirthdate());
        if (age == -1) {
            return null;
        }

        // Get other members of the household
        List<Person> householdMembers = getHouseholdMembers(person);

        return new ChildInfo(person.getFirstname(), person.getLastname(), age, householdMembers);
    }

    private List<Person> getHouseholdMembers(Person person) {
        return getPersonsByAddress(person.getAddress())
                .stream()
                .filter(p -> !p.getFirstname().equals(person.getFirstname()) && !p.getLastname().equals(person.getLastname()))
                .collect(Collectors.toList());
    }


}
