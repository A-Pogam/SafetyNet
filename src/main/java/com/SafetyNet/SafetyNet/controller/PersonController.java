package com.SafetyNet.SafetyNet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.SafetyNet.SafetyNet.model.Person;
import com.SafetyNet.SafetyNet.service.contracts.IPersonService;

@RestController
public class PersonController {

    @Autowired
    private IPersonService iPersonService;

    @GetMapping("/persons")
    public ResponseEntity<List<Person>> getAllPersons() {
        List<Person> persons = iPersonService.getAllPersons();

        if (!persons.isEmpty()) {
            return new ResponseEntity<>(persons, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/person")
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {
        Person addedPerson = iPersonService.addPerson(person);

        if (addedPerson != null) {
            return new ResponseEntity<>(addedPerson, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/persons/{firstName}/{lastName}")
    public ResponseEntity<Person> updatePerson(@PathVariable String firstName, @PathVariable String lastName, @RequestBody Person personUpdate) {
        Person updatedPerson  = iPersonService.updatePerson(firstName, lastName, personUpdate);

        if (updatedPerson != null) {
            return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/persons/{firstName}/{lastName}")
    public ResponseEntity<Void> deletePerson(@PathVariable String firstName, @PathVariable String lastName) {
        boolean deleted = iPersonService.deletePerson(firstName, lastName);

        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}