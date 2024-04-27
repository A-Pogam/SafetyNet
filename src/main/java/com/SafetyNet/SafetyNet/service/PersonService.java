package com.SafetyNet.SafetyNet.service;

import com.SafetyNet.SafetyNet.model.Person;
import org.springframework.stereotype.Service;
import com.SafetyNet.SafetyNet.repository.PersonRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final List<Person> personList = new ArrayList<>();


    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> getAllPersons() {
        return personList;
    }

    private final Map<String, Person> personMap = new HashMap<>();

    public void addPerson(Person person) {
        if (!personExists(person.getFirstname(), person.getLastname())) {
            personList.add(person);
        } else {
            throw new IllegalArgumentException("Person already exists");
        }
    }

    public Person updatePerson(Person updatedPerson) {
        // Recherche de la personne à mettre à jour dans la liste
        for (Person person : personList) {
            if (person.getFirstname().equals(updatedPerson.getFirstname()) && person.getLastname().equals(updatedPerson.getLastname())) {
                // Mettre à jour les informations de la personne
                person.setAddress(updatedPerson.getAddress());
                person.setCity(updatedPerson.getCity());
                person.setZip(updatedPerson.getZip());
                person.setPhone(updatedPerson.getPhone());
                person.setEmail(updatedPerson.getEmail());
                return person; // Retourne la personne mise à jour
            }
        }
        // Si la personne n'est pas trouvée dans la liste, lance une exception
        throw new IllegalArgumentException("Person not found");
    }



    public void deletePerson(String firstName, String lastName) {
        personRepository.deleteByFirstNameAndLastName(firstName, lastName);
    }

    public boolean personExists(String firstName, String lastName) {
        return personList.stream()
                .anyMatch(person -> person.getFirstname().equals(firstName) && person.getLastname().equals(lastName));
    }

    public Person getPersonByName(String firstName, String lastName) {
        return personRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    public List<Person> getPersonsByAddress(String address) {
        return personRepository.findByAddress(address);
    }

    public List<String> getEmailsByCity(String city) {
        return personRepository.findEmailsByCity(city);
    }
}
