package TestController;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.SafetyNet.SafetyNet.controller.ChildAlertController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.SafetyNet.SafetyNet.dto.ChildInfo;
import com.SafetyNet.SafetyNet.model.Person;
import com.SafetyNet.SafetyNet.service.contracts.IPersonService;

@WebMvcTest(controllers = ChildAlertController.class)
public class ChildAlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IPersonService iPersonService;

    private Person firstPerson = new Person("Geralt", "De Riv", "Kaer Morhen", "Kaedwen", "12345", "0102030405", "geralt.deriv@kaermorhen.kdw");
    private Person secondPerson = new Person("Vesemir", "De Kaer Morhen", "Kaer Morhen", "Kaedwen", "10101", "1100110011", "vesemir@kaermorhen.kdw");

    private ChildInfo firstChildInfo = new ChildInfo("Ciri", "Fiona Ellen Rianon", 14, new ArrayList<>(Arrays.asList(firstPerson, secondPerson)));

    @Test
    public void getChildAlert_returnOk() throws Exception {
        List<ChildInfo> childrenInfo = new ArrayList<>(Arrays.asList(firstChildInfo));

        when(iPersonService.getChildAlert(anyString()))
                .thenReturn(childrenInfo);

        mockMvc.perform(get("/childAlert")
                        .param("address", "Kaer Morhen")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].firstName").value("Ciri"))
                .andExpect(jsonPath("$.[0].lastName").value("Fiona Ellen Rianon"))
                .andExpect(jsonPath("$.[0].age").value(14))
                .andExpect(jsonPath("$.[0].householdMembers.[0].firstname").value("Geralt"))
                .andExpect(jsonPath("$.[0].householdMembers.[1].firstname").value("Vesemir"));
    }

    @Test
    public void getChildAlert_returnNotFound() throws Exception {
        when(iPersonService.getChildAlert(anyString()))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/childAlert")
                        .param("address", "Mahakam")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}