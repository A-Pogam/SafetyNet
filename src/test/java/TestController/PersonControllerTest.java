package TestController;

import com.SafetyNet.SafetyNet.SafetyNetApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import controller.PersonController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import service.PersonService;
import model.Person;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = SafetyNetApplication.class)
@AutoConfigureMockMvc
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllPersons() throws Exception {
        mockMvc.perform(get("/person"))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddPerson() throws Exception {
        // Créer une nouvelle personne
        Person person = new Person("John", "Doe", "123 Main St", "Anytown", "12345", "555-555-5555", "john@example.com");
        String personJson = "{\"firstname\":\"John\",\"lastname\":\"Doe\",\"address\":\"123 Main St\",\"city\":\"Anytown\",\"zip\":\"12345\",\"phone\":\"555-555-5555\",\"email\":\"john@example.com\"}";

        // Mock du service pour ajouter une personne
        when(personService.addPerson(person)).thenReturn(person);

        // Tester l'ajout de personne via le contrôleur
        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJson))
                .andExpect(status().isCreated()) // Vérifier le statut créé (201)
                .andExpect(jsonPath("$.firstname").value("John"))
                .andExpect(jsonPath("$.lastname").value("Doe"));
    }


    @Test
    void testUpdatePerson() throws Exception {
        Person updatedPerson = new Person("John", "Doe", "123 Main St", "Springfield", "12345", "123-456-7890", "john.doe@example.com");

        when(personService.updatePerson(updatedPerson)).thenReturn(updatedPerson);

        mockMvc.perform(put("/person/John/Doe")  // Utilisation de l'URI pour mettre à jour une personne
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPerson)))
                .andExpect(status().isOk())
                .andExpect(content().string("Person updated successfully"));
    }


    @Test
    void testDeletePerson() throws Exception {
        when(personService.deletePerson("John", "Doe")).thenReturn(true);

        mockMvc.perform(delete("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Person("John", "Doe", null, null, null, null, null))))
                .andExpect(status().isNoContent())
                .andExpect(content().string("Person deleted successfully"));
    }
}
