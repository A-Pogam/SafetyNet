package com.SafetyNet.SafetyNet.repository.contracts;

import com.SafetyNet.SafetyNet.model.Person;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPersonRepository {

    List<Person> findAll();
    List<Person> findByAddress(String address);
    List<Person> findHouseholdMembersByPerson(Person person);
    List<String> findEmailsByCity(String city);
    List<String> findPhonesFromPersonList(List<Person> personList);
    Person findByFirstNameAndLastName(String firstName, String lastName);

    Person save(Person person);
    Person update(Person existingPerson, Person personUpdate);

    void deleteByFirstNameAndLastName(String firstName, String lastName);
}