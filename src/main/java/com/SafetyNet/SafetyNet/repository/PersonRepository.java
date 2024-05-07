package com.SafetyNet.SafetyNet.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.SafetyNet.SafetyNet.model.Person;
import com.SafetyNet.SafetyNet.repository.contracts.IPersonRepository;

@Repository
public class PersonRepository implements IPersonRepository {

    private static final Logger logger = LoggerFactory.getLogger(PersonRepository.class);

    private List<Person> persons = new ArrayList<>();

    @Override
    public List<Person> findAll() {
        logger.debug("Searching for all persons.");
        return new ArrayList<>(persons);
    }

    @Override
    public List<Person> findByAddress(String address) {
        // Implémentation de la recherche par adresse
        logger.debug("Searching for persons at the address: {}.", address);
        return persons.stream()
                .filter(person -> person.getAddress().equalsIgnoreCase(address))
                .collect(Collectors.toList());
    }

    @Override
    public List<Person> findHouseholdMembersByPerson(Person person) {
        logger.debug("Searching for all persons living with: {} {}.", person.getFirstname(), person.getLastname());
        return findByAddress(person.getAddress())
                .stream()
                .filter(p -> !p.getFirstname().equals(person.getFirstname()) && !p.getLastname().equals(person.getLastname()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findEmailsByCity(String city) {
        // Implémentation de la recherche des emails par ville
        logger.debug("Searching for persons email in city: {}.", city);
        return persons.stream()
                .filter(person -> person.getCity().equalsIgnoreCase(city))
                .map(Person::getEmail)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findPhonesFromPersonList(List<Person> personList) {
        logger.debug("Searching for persons phone numbers.");
        return persons.stream()
                .filter(person -> personList.contains(person))
                .map(Person::getPhone)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public Person findByFirstNameAndLastName(String firstName, String lastName) {
        // Implémentation de la recherche par prénom et nom
        logger.info("Searching for person with name: {} {}.", firstName, lastName);
        return persons.stream()
                .filter(person -> person.getFirstname().equalsIgnoreCase(firstName) && person.getLastname().equalsIgnoreCase(lastName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Person save(Person person) {
        logger.debug("Adding person: {} {}.", person.getFirstname(), person.getLastname());
        persons.add(person);
        logger.info("Person added successfully: {} {}.", person.getFirstname(), person.getLastname());

        return person;
    }

    @Override
    public Person update(Person existingPerson, Person personUpdate) {
        logger.debug("Updating medical record for person with name: {} {}.", existingPerson.getFirstname(), existingPerson.getLastname());

        if (personUpdate.getAddress() != null) {
            existingPerson.setAddress(personUpdate.getAddress());
        }
        if (personUpdate.getCity() != null) {
            existingPerson.setCity(personUpdate.getCity());
        }
        if (personUpdate.getZip() != null) {
            existingPerson.setZip(personUpdate.getZip());
        }
        if (personUpdate.getPhone() != null) {
            existingPerson.setPhone(personUpdate.getPhone());
        }
        if (personUpdate.getEmail() != null) {
            existingPerson.setEmail(personUpdate.getEmail());
        }

        logger.info("Person updated successfully: {} {}.", existingPerson.getFirstname(), existingPerson.getLastname());
        return existingPerson;
    }

    @Override
    public void deleteByFirstNameAndLastName(String firstName, String lastName) {
        logger.debug("Deleting person with name: {} {}.", firstName, lastName);
        persons.removeIf(person -> person.getFirstname().equalsIgnoreCase(firstName) && person.getLastname().equalsIgnoreCase(lastName));
        logger.info("Person(s) deleted successfully: {} {}.", firstName, lastName);
    }
}