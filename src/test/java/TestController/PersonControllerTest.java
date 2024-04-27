package TestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.SafetyNet.SafetyNet.controller.PersonController;
import com.SafetyNet.SafetyNet.model.Person;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import com.SafetyNet.SafetyNet.service.PersonService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PersonService personService;

    @InjectMocks
    private PersonController personController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetAllPersons() throws Exception {
        List<Person> persons = new ArrayList<>();
        persons.add(new Person("John", "Doe", "123 Main St", "Anytown", "12345", "555-555-5555", "john@example.com"));

        when(personService.getAllPersons()).thenReturn(persons);

        mockMvc.perform(get("/person"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstname").value("John"));
    }


    @Test
    void testAddPerson_Success() throws Exception {
        // Création d'un objet Person pour simuler l'ajout
        Person person = new Person("John", "Doe", "123 Main St", "Anytown", "12345", "555-555-5555", "john@example.com");

        // Configuration du comportement du mock
        when(personService.personExists(any(String.class), any(String.class))).thenReturn(false);

        // Effectuer la requête POST vers l'endpoint /person pour ajouter la personne
        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                // Vérifier que la réponse a un statut HTTP 201 (Created)
                .andExpect(status().isCreated())
                // Vérifier que le corps de la réponse contient le message "Person added successfully"
                .andExpect(content().string("Person added successfully"));
    }

    @Test
    void testAddPerson_Failure() throws Exception {
        Person person = new Person("John", "Doe", "123 Main St", "Anytown", "12345", "555-555-5555", "john@example.com");

        when(personService.personExists(person.getFirstname(), person.getLastname())).thenReturn(true);

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Person already exists"));
    }

    @Test
    void testUpdatePerson() throws Exception {
        Person updatedPerson = new Person("John", "Doe", "123 Main St", "Springfield", "12345", "123-456-7890", "john.doe@example.com");

        when(personService.updatePerson(any(Person.class))).thenReturn(updatedPerson);

        mockMvc.perform(put("/person/John/Doe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPerson)))
                .andExpect(status().isOk())
                .andExpect(content().string("Person updated successfully"));
    }

    @Test
    void testDeletePerson() throws Exception {
        mockMvc.perform(delete("/person/John/Doe"))
                .andExpect(status().isOk())
                .andExpect(content().string("Person deleted successfully"));
    }
}
