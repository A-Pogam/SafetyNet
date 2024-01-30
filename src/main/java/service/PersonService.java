package service;


import model.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final List<Person> persons = new ArrayList<>();

    public List<Person> getAllPersons() {
        return new ArrayList<>(persons);
    }

    public Person addPerson(Person person) {
        if (!personExists(person)) {
            persons.add(person);
            return person;
        } else {
            return null;
        }
    }

    public Person updatePerson(Person updatedPerson) {
        Person existingPerson = getPersonByName(updatedPerson.getFirstname(), updatedPerson.getLastname());
        if (existingPerson != null) {
            existingPerson.setAddress(updatedPerson.getAddress());
            existingPerson.setCity(updatedPerson.getCity());
            existingPerson.setZip(updatedPerson.getZip());
            existingPerson.setPhone(updatedPerson.getPhone());
            existingPerson.setEmail(updatedPerson.getEmail());
            return existingPerson;
        } else {
            return null;
        }
    }

    public boolean deletePerson(String firstName, String lastName) {
        List<Person> matchingPersons = persons.stream()
                .filter(person -> person.getFirstname().equals(firstName) && person.getLastname().equals(lastName))
                .collect(Collectors.toList());

        if (!matchingPersons.isEmpty()) {
            persons.removeAll(matchingPersons);
            return true;
        } else {
            return false;
        }
    }

    private boolean personExists(Person targetPerson) {
        return getPersonByName(targetPerson.getFirstname(), targetPerson.getLastname()) != null;
    }

    private Person getPersonByName(String firstName, String lastName) {
        return persons.stream()
                .filter(person -> person.getFirstname().equals(firstName) && person.getLastname().equals(lastName))
                .findFirst()
                .orElse(null);
    }
}

