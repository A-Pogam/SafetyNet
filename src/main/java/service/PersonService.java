package service;

import model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.PersonRepository;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person addPerson(Person person) {
        if (!personRepository.existsByFirstNameAndLastName(person.getFirstname(), person.getLastname())) {
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
        return personRepository.existsByFirstNameAndLastName(firstName, lastName);
    }
}


