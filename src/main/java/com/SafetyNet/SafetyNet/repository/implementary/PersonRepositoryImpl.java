package com.SafetyNet.SafetyNet.repository.implementary;

import com.SafetyNet.SafetyNet.model.Person;
import com.SafetyNet.SafetyNet.repository.PersonRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PersonRepositoryImpl implements PersonRepository {

    private final List<Person> persons = new ArrayList<>();

    @Override
    public List<Person> findAll() {
        return persons;
    }

    @Override
    public Person findByFirstNameAndLastName(String firstName, String lastName) {
        // Implémentation de la recherche par prénom et nom
        return null;
    }

    @Override
    public List<Person> findByAddress(String address) {
        // Implémentation de la recherche par adresse
        return null;
    }

    @Override
    public List<String> findEmailsByCity(String city) {
        // Implémentation de la recherche des emails par ville
        return null;
    }

    @Override
    public void save(Person person) {
        // Implémentation de la sauvegarde d'une personne
        persons.add(person);
    }

    @Override
    public void deleteByFirstNameAndLastName(String firstName, String lastName) {
        // Implémentation de la suppression d'une personne par prénom et nom
        // À implémenter selon vos besoins
    }
}
