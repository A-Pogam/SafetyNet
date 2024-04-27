package com.SafetyNet.SafetyNet.repository;

import com.SafetyNet.SafetyNet.model.Person;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository {

    List<Person> findAll();
    Person findByFirstNameAndLastName(String firstName, String lastName);
    List<Person> findByAddress(String address);
    List<String> findEmailsByCity(String city);
    void save(Person person);
    void deleteByFirstNameAndLastName(String firstName, String lastName);
}
