package com.SafetyNet.SafetyNet.dto;

import com.SafetyNet.SafetyNet.model.Person;

public class CoveredPerson {

    private Person person;
    private int age;

    public CoveredPerson(Person person, int age) {
        this.person = person;
        this.age = age;
    }

    public Person getPerson() {
        return person;
    }

    public int getAge() {
        return age;
    }
}
