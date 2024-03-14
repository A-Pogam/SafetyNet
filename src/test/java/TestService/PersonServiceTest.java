package TestService;

import model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.PersonService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import model.Person;
import service.PersonService;
import java.util.ArrayList;
import java.util.List;



public class PersonServiceTest {

    @Mock
    private List<Person> persons;

    @InjectMocks
    private PersonService personService;

    @BeforeEach
    void setUp() {
        personService = mock(PersonService.class);
        persons = new ArrayList<>();
        // Créer quelques personnes pour les tests
        persons.add(new Person("John", "Doe", "123 Main St", "Légume", "12", "1234", "john@example.com"));
        persons.add(new Person("Jane", "Doe", "12 rue de la courgette", "Légume", "12", "1234", "jane@exemple.com"));
    }

    @Test
    void testGetAllPersons() {
        // Créer une instance de PersonService à tester
        PersonService personService = new PersonService();

        // Ajouter quelques personnes à la liste persons
        Person person1 = new Person("John", "Doe", "123 Main St", "Légume", "12", "1234", "john@exemple.com");
        Person person2 = new Person("Jane", "Doe", "12 rue de la courgette", "Légume", "12", "1234", "jane@exemple.com");
        personService.addPerson(person1);
        personService.addPerson(person2);

        // Appeler la méthode à tester
        List<Person> result = personService.getAllPersons();

        // Vérifier le résultat
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetPersonByName() {
        // Données de test
        String firstName = "John";
        String lastName = "Doe";
        // Mock de la méthode getPersonByName

        PersonService personServiceMock = mock(PersonService.class);
        when(personServiceMock.getPersonByName(firstName, lastName)).thenReturn(persons.get(0));

        // Appeler la méthode à tester
        Person result = personServiceMock.getPersonByName(firstName, lastName);

        // Vérifier le résultat
        assertNotNull(result);
        assertEquals(firstName, result.getFirstname());
        assertEquals(lastName, result.getLastname());
    }

    @Test
    void testDeletePerson() {
        // Données de test
        String firstName = "John";
        String lastName = "Doe";

        // Définir le comportement du mock pour la méthode deletePerson
        when(personService.deletePerson(firstName, lastName)).thenReturn(true);

        // Appeler la méthode à tester
        boolean result = personService.deletePerson(firstName, lastName);

        // Vérifier le résultat
        assertTrue(result);
        verify(personService, times(1)).deletePerson(firstName, lastName);
    }

    @Test
    void testUpdatePerson() {
        // Données de test
        Person personToUpdate = new Person("John", "Doe", "123 Main St", "Légume", "12", "1234", "john@example.com");

        // Définir le comportement du mock pour la méthode updatePerson
        when(personService.updatePerson(personToUpdate)).thenReturn(personToUpdate);

        // Appeler la méthode à tester
        Person result = personService.updatePerson(personToUpdate);

        // Vérifier le résultat
        assertNotNull(result);
        assertEquals(personToUpdate, result);
        verify(personService, times(1)).updatePerson(personToUpdate);
    }



}
