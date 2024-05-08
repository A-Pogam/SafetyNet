package com.SafetyNet.SafetyNet.TestService;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.SafetyNet.SafetyNet.dto.ChildInfo;
import com.SafetyNet.SafetyNet.model.Person;
import com.SafetyNet.SafetyNet.service.contracts.IPersonService;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonServiceIT {

    @Autowired
    private IPersonService iPersonService;

    @Test
    public void getAllPersons_returnPersons() {
        List<Person> persons = iPersonService.getAllPersons();

        assertThat(persons).isNotEmpty();
        assertThat(persons.getFirst().getFirstname()).isEqualTo("John");
        assertThat(persons.getFirst().getLastname()).isEqualTo("Boyd");
        assertThat(persons.getFirst().getAddress()).isEqualTo("1509 Culver St");
        assertThat(persons.getFirst().getCity()).isEqualTo("Culver");
        assertThat(persons.getFirst().getZip()).isEqualTo("97451");
        assertThat(persons.getFirst().getPhone()).isEqualTo("841-874-6512");
        assertThat(persons.getFirst().getEmail()).isEqualTo("jaboyd@email.com");
    }

    @Test
    public void addPerson_returnPerson() {
        Person newPerson = new Person("John", "Doe", "1A, Common St", "Raccoun", "12345", "101-101-1010", "john.doe@email.com");

        Person personAdded = iPersonService.addPerson(newPerson);

        assertThat(personAdded.getFirstname()).isEqualTo(newPerson.getFirstname());
        assertThat(personAdded.getLastname()).isEqualTo(newPerson.getLastname());
        assertThat(personAdded.getAddress()).isEqualTo(newPerson.getAddress());
        assertThat(personAdded.getCity()).isEqualTo(newPerson.getCity());
        assertThat(personAdded.getZip()).isEqualTo(newPerson.getZip());
        assertThat(personAdded.getPhone()).isEqualTo(newPerson.getPhone());
        assertThat(personAdded.getEmail()).isEqualTo(newPerson.getEmail());
    }

    @Test
    public void addPerson_returnNull() {
        Person newPerson = new Person("Eric", "Cadigan", "951 LoneTree Rd", "Culver", "97451", "841-874-7458", "gramps@email.com");

        Person personAdded = iPersonService.addPerson(newPerson);

        assertThat(personAdded).isNull();
    }

    @Test
    public void updatePerson_returnPerson() {
        Person personToUpdate = new Person(null, null, "446 ,Niwhsreg Ric", "Revulc", "15479", "2156-478-148", "dyobaj@liame.moc");

        Person personUpdated = iPersonService.updatePerson("Peter", "Duncan", personToUpdate);

        assertThat(personUpdated.getFirstname()).isEqualTo("Peter");
        assertThat(personUpdated.getLastname()).isEqualTo("Duncan");
        assertThat(personUpdated.getAddress()).isEqualTo(personToUpdate.getAddress());
        assertThat(personUpdated.getCity()).isEqualTo(personToUpdate.getCity());
        assertThat(personUpdated.getZip()).isEqualTo(personToUpdate.getZip());
        assertThat(personUpdated.getPhone()).isEqualTo(personToUpdate.getPhone());
        assertThat(personUpdated.getEmail()).isEqualTo(personToUpdate.getEmail());
    }

    @Test
    public void updatePerson_returnNull() {
        Person personToUpdate = new Person();

        Person personUpdated = iPersonService.updatePerson("John", "Cadigan", personToUpdate);

        assertThat(personUpdated).isNull();
    }

    @Test
    public void deletePerson_returnTrue() {
        boolean isDeleted = iPersonService.deletePerson("Jonanathan", "Marrack");
        assertThat(isDeleted).isTrue();
    }

    @Test
    public void deletePerson_returnFalse() {
        boolean isDeleted = iPersonService.deletePerson("John", "Cadigan");
        assertThat(isDeleted).isFalse();
    }

    @Test
    public void getChildAlert_returnChildrenInfos() {
        List<ChildInfo> childrenInfos = iPersonService.getChildAlert("1509 Culver St");

        assertThat(childrenInfos).isNotEmpty();
        assertThat(childrenInfos.getFirst().getFirstName()).isEqualTo("Tenley");
        assertThat(childrenInfos.getFirst().getLastName()).isEqualTo("Boyd");
        assertThat(childrenInfos.getFirst().getAge()).isPositive();
        assertThat(childrenInfos.getFirst().getHouseholdMembers()).isNotEmpty();
        assertThat(childrenInfos.getFirst().getHouseholdMembers().getFirst().getFirstname()).isNotBlank();
        assertThat(childrenInfos.getFirst().getHouseholdMembers().getFirst().getLastname()).isNotBlank();
        assertThat(childrenInfos.getFirst().getHouseholdMembers().getFirst().getAddress()).isNotBlank();
        assertThat(childrenInfos.getFirst().getHouseholdMembers().getFirst().getCity()).isNotBlank();
        assertThat(childrenInfos.getFirst().getHouseholdMembers().getFirst().getZip()).isNotBlank();
        assertThat(childrenInfos.getFirst().getHouseholdMembers().getFirst().getPhone()).isNotBlank();
        assertThat(childrenInfos.getFirst().getHouseholdMembers().getFirst().getEmail()).isNotBlank();
    }

    @Test
    public void getEmailsByCity_returnEmailsByCity() {
        List<String> emailsByCity = iPersonService.getEmailsByCity("Culver");

        assertThat(emailsByCity).isNotEmpty();
        assertThat(emailsByCity.getFirst()).isNotBlank();
        assertThat(emailsByCity.getLast()).isEqualTo("gramps@email.com");
    }

    @Test
    public void getPersonInfo_returnPersonDetails() {
        Map<String, Object> personDetails = iPersonService.getPersonInfo("Jacob", "Boyd");

        assertThat(personDetails.get("name")).isEqualTo("Jacob Boyd");
        assertThat(personDetails.get("address")).isEqualTo("1509 Culver St");
        assertThat(personDetails.get("phone")).isEqualTo("841-874-6513");
        assertThat(personDetails.get("email")).isEqualTo("drk@email.com");
        assertThat(personDetails.get("age")).isNotNull();
        assertThat(personDetails.get("medications")).isEqualTo(new ArrayList<>(Arrays.asList("pharmacol:5000mg", "terazine:10mg", "noznazol:250mg")));
        assertThat(personDetails.get("allergies")).isEqualTo(new ArrayList<>());
    }

    @Test
    public void getPersonInfo_returnNull() {
        Map<String, Object> personDetails = iPersonService.getPersonInfo("John", "Cadigan");
        assertThat(personDetails).isNull();
    }
}