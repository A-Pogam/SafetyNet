package Service;

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

    public void saveAll(List<Person> persons) {
        personRepository.saveAll(persons);
    }

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public Person getPersonByID(Long id) {
        return personRepository.findById(id).orElse(null);
    }
}


