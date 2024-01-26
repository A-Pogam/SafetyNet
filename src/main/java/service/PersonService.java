package service;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import model.Person;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {

    private final String jsonFilePath = "src/main/resources/data/safety-net-data.json";
    private final Jsonb jsonb = JsonbBuilder.create();

    public List<Person> getAllPersons() {
        try {
            // Read data from JSON file
            File jsonFile = new File(jsonFilePath);
            if (jsonFile.exists()) {
                FileReader fileReader = new FileReader(jsonFile);
                List<Person> persons = jsonb.fromJson(
                        fileReader,
                        new ArrayList<Person>(){}.getClass().getGenericSuperclass()
                );
                fileReader.close();
                return persons != null ? persons : new ArrayList<>();
            } else {
                return new ArrayList<>();
            }
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
        try (FileWriter fileWriter = new FileWriter(jsonFilePath)) {
            // Write data to JSON file
            jsonb.toJson(persons, fileWriter);
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
