package TestService;

import com.SafetyNet.SafetyNet.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.SafetyNet.SafetyNet.repository.PersonRepository;
import com.SafetyNet.SafetyNet.service.PersonService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testUpdatePerson() {
        // Créer une personne à mettre à jour
        Person personToUpdate = new Person("John", "Doe", "123 Main St", "Anytown", "12345", "555-555-5555", "john@example.com");

        // Ajouter la personne à la liste personList
        personService.addPerson(personToUpdate);

        // Définir le comportement du mock pour la méthode findByFirstNameAndLastName de PersonRepository
        when(personRepository.findByFirstNameAndLastName("John", "Doe")).thenReturn(personToUpdate);

        // Appeler la méthode à tester
        Person updatedPerson = personService.updatePerson(personToUpdate);

        // Vérifier que la méthode save n'a pas été appelée (car la personne est mise à jour en mémoire, pas dans la base de données)
        verify(personRepository, never()).save(any());

        // Vérifier que la personne retournée par la méthode est la même que celle passée en paramètre
        assertEquals(personToUpdate, updatedPerson);
    }






    @Test
    void testDeletePerson() {
        // Données de test
        String firstName = "John";
        String lastName = "Doe";

        // Définir le comportement du mock pour la méthode deleteByFirstNameAndLastName de PersonRepository
        doNothing().when(personRepository).deleteByFirstNameAndLastName(firstName, lastName);

        // Appeler la méthode à tester
        personService.deletePerson(firstName, lastName);

        // Vérifier l'appel à la méthode deleteByFirstNameAndLastName
        verify(personRepository, times(1)).deleteByFirstNameAndLastName(firstName, lastName);
    }
}
