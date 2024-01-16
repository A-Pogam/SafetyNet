package service;

import model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.PersonRepository;

import java.util.List;


@Service
public class PersonService {
    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person addPerson(Person person) {
        if (!personRepository.existByFirstNameAndLastName(person.getFirstname(), person.getLastname())) {
            return personRepository.save(person);
        } else {
            return null;
        }

    }

    public Person updatePerson (Person person) {
        return personRepository.save(person);
    }

    public boolean deletePerson(String firstName, String lastName) {
        personRepository.deleteByFirstNameAndLastName(firstName, lastName);
        return !personRepository.existByFirstNameAndLastName(firstName, lastName);
    }



    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public Person getPersonByID(Long id) {
        return personRepository.findById(id).orElse(null);
    }

    public List<Person> getPersonsbyCity(String city) {
        return personRepository.findByCity(city);
    }

    public List<Person> getChildAlertByAddress(String address) {
        return null;
    }
}


