package service;


import model.MedicalRecord;
import model.Person;
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
        if (!personExists(person)) {
            persons.add(person);
            logger.info("Person added successfully");
            return person;
        } else {
            logger.warn("Person already exists: {}", person);
            return null;
        }
    }


    public Person updatePerson(Person updatedPerson) {
        logger.info("Updating person: {}", updatedPerson);
        Person existingPerson = getPersonByName(updatedPerson.getFirstname(), updatedPerson.getLastname());
        if (existingPerson != null) {
            existingPerson.setAddress(updatedPerson.getAddress());
            existingPerson.setCity(updatedPerson.getCity());
            existingPerson.setZip(updatedPerson.getZip());
            existingPerson.setPhone(updatedPerson.getPhone());
            existingPerson.setEmail(updatedPerson.getEmail());
            logger.info("Person updated successfully");
            return existingPerson;
        } else {
            logger.warn("Person not found for update: {}", updatedPerson);
            return null;
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

    private boolean personExists(Person targetPerson) {
        return persons.stream()
                .anyMatch(person -> person.getFirstname().equals(targetPerson.getFirstname())
                        && person.getLastname().equals(targetPerson.getLastname()));
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



}
