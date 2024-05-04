package TestController;

import com.SafetyNet.SafetyNet.SafetyNetApplication;
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

@SpringBootTest(classes = SafetyNetApplication.class)
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
        // Given
        List<Person> persons = new ArrayList<>();
        persons.add(new Person("John", "Doe", "123 Main St", "Anytown", "12345", "555-555-5555", "john@example.com"));
        when(personService.getAllPersons()).thenReturn(persons);

        // When & Then
        mockMvc.perform(get("/person"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstname").value("John"));
    }

    @Test
    void testAddPerson_Success() throws Exception {
        // Given
        Person person = new Person("John", "Doe", "123 Main St", "Anytown", "12345", "555-555-5555", "john@example.com");
        when(personService.personExists(any(String.class), any(String.class))).thenReturn(false);

        // When & Then
        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Person added successfully"));
    }

    @Test
    void testAddPerson_Failure() throws Exception {
        // Given
        Person person = new Person("John", "Doe", "123 Main St", "Anytown", "12345", "555-555-5555", "john@example.com");

        // Configurer le comportement du mock pour retourner vrai, indiquant que la personne existe déjà
        when(personService.personExists(person.getFirstname(), person.getLastname())).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                // S'attendre à un statut HTTP 400 (Bad Request)
                .andExpect(status().isBadRequest())
                // Vérifier que le corps de la réponse contient le message "Person already exists"
                .andExpect(content().string("Person already exists"));
    }

    @Test
    void testUpdatePerson() throws Exception {
        // Given
        Person updatedPerson = new Person("John", "Doe", "123 Main St", "Springfield", "12345", "123-456-7890", "john.doe@example.com");

        // Configurer le comportement du mock pour la méthode updatePerson de PersonService
        when(personService.updatePerson("John", "Doe", updatedPerson)).thenReturn(true);

        // When & Then
        mockMvc.perform(put("/person/John/Doe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPerson)))
                .andExpect(status().isOk())
                .andExpect(content().string("Person updated successfully"));
    }

    @Test
    void testDeletePerson() throws Exception {
        // Given
        String firstName = "John";
        String lastName = "Doe";

        // When & Then
        mockMvc.perform(delete("/person/{firstName}/{lastName}", firstName, lastName))
                .andExpect(status().isNoContent());
    }
}