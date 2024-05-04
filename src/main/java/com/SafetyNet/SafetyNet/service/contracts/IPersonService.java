
package com.SafetyNet.SafetyNet.service.contracts;

import java.util.List;
import java.util.Map;

import com.SafetyNet.SafetyNet.dto.ChildInfo;
import com.SafetyNet.SafetyNet.model.Person;

public interface IPersonService {

    public List<Person> getAllPersons();

    public Person addPerson(Person person);
    public Person updatePerson(String firstName, String lastName, Person personUpdate);

    public boolean deletePerson(String firstName, String lastName);

    public List<ChildInfo> getChildAlert(String address);
    public List<String> getEmailsByCity(String city);
    public Map<String, Object> getPersonInfo(String firstName, String lastName);
}
