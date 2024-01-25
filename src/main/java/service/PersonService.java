package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Person;
import org.springframework.stereotype.Service;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {

    private final String jsonFilePath = "src/main/resources/data/safety-net-data.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Person> getAllPersons() {
        try {
            // Read data from JSON file
            List<Person> persons = objectMapper.readValue(new File(jsonFilePath),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Person.class));
            return persons != null ? persons : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Person addPerson(Person person) {
        List<Person> persons = getAllPersons();
        if (!personExists(persons, person)) {
            persons.add(person);
            savePersons(persons);
            return person;
        } else {
            return null;
        }
    }

    public Person updatePerson(Person updatedPerson) {
        List<Person> persons = getAllPersons();
        for (int i = 0; i < persons.size(); i++) {
            Person existingPerson = persons.get(i);
            if (existingPerson.getFirstname().equals(updatedPerson.getFirstname()) &&
                    existingPerson.getLastname().equals(updatedPerson.getLastname())) {
                existingPerson.setAddress(updatedPerson.getAddress());
                existingPerson.setCity(updatedPerson.getCity());
                existingPerson.setZip(updatedPerson.getZip());
                existingPerson.setPhone(updatedPerson.getPhone());
                existingPerson.setEmail(updatedPerson.getEmail());
                savePersons(persons);
                return updatedPerson;
            }
        }
        return null;
    }

    public boolean deletePerson(String firstName, String lastName) {
        List<Person> persons = getAllPersons();
        persons.removeIf(person ->
                person.getFirstname().equals(firstName) && person.getLastname().equals(lastName));
        savePersons(persons);
        return !personExists(persons, new Person(firstName, lastName));
    }

    private void savePersons(List<Person> persons) {
        try {
            // Write data to JSON file
            objectMapper.writeValue(new File(jsonFilePath), persons);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean personExists(List<Person> persons, Person targetPerson) {
        return persons.stream()
                .anyMatch(person ->
                        person.getFirstname().equals(targetPerson.getFirstname()) &&
                                person.getLastname().equals(targetPerson.getLastname()));
    }
}
