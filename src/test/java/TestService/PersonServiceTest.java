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

        // Créer une personne avec les informations mises à jour
        Person updatedPersonInfo = new Person("John", "Doe", "456 Elm St", "Newtown", "54321", "555-555-5555", "john@example.com");

        // Appeler la méthode à tester
        boolean updated = personService.updatePerson("John", "Doe", updatedPersonInfo);

        // Vérifier que la personne a été mise à jour avec succès
        assertTrue(updated);

        // Récupérer la personne mise à jour
        Person updatedPerson = personService.getPersonByName("John", "Doe");

        // Vérifier que les informations de la personne ont été correctement mises à jour
        assertEquals("456 Elm St", updatedPerson.getAddress());
        assertEquals("Newtown", updatedPerson.getCity());
        assertEquals("54321", updatedPerson.getZip());
    }





    @Test
    void testDeletePerson() {
        // Données de test
        String firstName = "John";
        String lastName = "Doe";

        // Créer une personne à supprimer
        Person personToDelete = new Person(firstName, lastName, "123 Main St", "Anytown", "12345", "555-555-5555", "john@example.com");

        // Ajouter la personne à la liste persons
        personService.addPerson(personToDelete);

        // Appeler la méthode à tester
        boolean deleted = personService.deletePerson(firstName, lastName);

        // Vérifier que la méthode a renvoyé vrai
        assertTrue(deleted);

        // Vérifier que la personne a été supprimée de la liste persons
        assertFalse(personService.personExists(firstName, lastName));
    }

}
