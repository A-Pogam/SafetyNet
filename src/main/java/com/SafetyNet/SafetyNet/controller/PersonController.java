package com.SafetyNet.SafetyNet.controller;

import com.SafetyNet.SafetyNet.service.PersonService;
import com.SafetyNet.SafetyNet.model.Person;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public ResponseEntity<List<Person>> getAllPersons() {
        List<Person> persons = personService.getAllPersons();
        return ResponseEntity.ok(persons);
    }

    @PostMapping
    public ResponseEntity<String> addPerson(@RequestBody Person person) {
        if (!personService.personExists(person.getFirstname(), person.getLastname())) {
            personService.addPerson(person);
            return ResponseEntity.status(HttpStatus.CREATED).body("Person added successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Person already exists");
        }
    }


    @PutMapping("/{firstName}/{lastName}")
    public ResponseEntity<String> updatePerson(
            @PathVariable String firstName,
            @PathVariable String lastName,
            @RequestBody Person updatedPerson) {
        updatedPerson.setFirstname(firstName);
        updatedPerson.setLastname(lastName);
        personService.updatePerson(updatedPerson);
        return ResponseEntity.ok("Person updated successfully");
    }

    @DeleteMapping("/{firstName}/{lastName}")
    public ResponseEntity<String> deletePerson(
            @PathVariable String firstName,
            @PathVariable String lastName) {
        personService.deletePerson(firstName, lastName);
        return ResponseEntity.ok("Person deleted successfully");
    }
}
