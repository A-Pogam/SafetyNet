package dto;

import model.Person;

import java.util.List;

public class ChildInfo {
    private String firstName;
    private String lastName;
    private int age;
    private List<Person> householdMembers;

    public ChildInfo(String firstName, String lastName, int age, List<Person> householdMembers) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.householdMembers = householdMembers;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Person> getHouseholdMembers() {
        return householdMembers;
    }

    public void setHouseholdMembers(List<Person> householdMembers) {
        this.householdMembers = householdMembers;
    }
}

