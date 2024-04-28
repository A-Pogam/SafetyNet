package com.SafetyNet.SafetyNet.repository.implementary;

import com.SafetyNet.SafetyNet.model.Person;
import com.SafetyNet.SafetyNet.repository.PersonRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PersonRepositoryImpl implements PersonRepository {

    private final List<Person> persons = new ArrayList<>();

    @Override
    public List<Person> findAll() {
        return new ArrayList<>(persons);
    }

    @Override
    public Person findByFirstNameAndLastName(String firstName, String lastName) {
        // Implémentation de la recherche par prénom et nom
        return persons.stream()
                .filter(person -> person.getFirstname().equals(firstName) && person.getLastname().equals(lastName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Person> findByAddress(String address) {
        // Implémentation de la recherche par adresse
        return persons.stream()
                .filter(person -> person.getAddress().equalsIgnoreCase(address))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findEmailsByCity(String city) {
        // Implémentation de la recherche des emails par ville
        return persons.stream()
                .filter(person -> person.getCity().equalsIgnoreCase(city))
                .map(Person::getEmail)
                .collect(Collectors.toList());
    }

    @Override
    public void save(Person person) {
        // Vérifier si la personne existe déjà avant de l'ajouter
        if (!persons.contains(person)) {
            persons.add(person);
        } else {
            throw new IllegalArgumentException("Person already exists");
        }
    }

    @Override
    public void deleteByFirstNameAndLastName(String firstName, String lastName) {
        // Implémentation de la suppression d'une personne par prénom et nom
        persons.removeIf(person -> person.getFirstname().equals(firstName) && person.getLastname().equals(lastName));
    }
}
