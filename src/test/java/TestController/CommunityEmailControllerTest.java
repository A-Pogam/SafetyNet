/*package TestController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import controller.CommunityEmailController;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import service.PersonService;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes = CommunityEmailController.class)
public class CommunityEmailControllerTest {

    @MockBean
    private PersonService personService;

    @InjectMocks
    private CommunityEmailController communityEmailController;

    @Test
    public void testGetEmailsByCity_Success() {
        // Given
        String city = "Anytown";
        List<String> emails = new ArrayList<>();
        emails.add("email1@example.com");
        emails.add("email2@example.com");
        when(personService.getEmailsByCity(city)).thenReturn(emails);

        // When
        ResponseEntity<List<String>> responseEntity = communityEmailController.getEmailsByCity(city);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(emails, responseEntity.getBody());
    }

    @Test
    public void testGetEmailsByCity_NoEmails() {
        // Given
        String city = "UnknownCity";
        when(personService.getEmailsByCity(city)).thenReturn(null);

        // When
        ResponseEntity<List<String>> responseEntity = communityEmailController.getEmailsByCity(city);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }
} */
