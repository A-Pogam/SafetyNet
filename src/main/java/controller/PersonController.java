package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import service.PersonService;
import model.Person;


@Controller
@RequestMapping("/persons")
public class PersonController {
    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personservice) {
        this.personService = personservice;
    }

    @PostMapping
    public ResponseEntity<String> addPerson(@RequestBody Person person) {
        Person addedPerson = personService.addPerson(person);
        if (addedPerson != null) {
            return new ResponseEntity<>("Person added successfully", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Failed to add this person", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<String> updatePerson(@RequestBody Person person) {
        Person updatedPerson = personService.updatePerson(person);
        if (updatedPerson != null) {
            return new ResponseEntity<>("Person updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to update this person", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{firstName}/{lastName}")
    public ResponseEntity<String> deletePerson(@PathVariable String firstName, @PathVariable String lastName) {
        boolean deleted = personService.deletePerson(firstName, lastName);
        if (deleted) {
         return new ResponseEntity<>("person deleted successfully", HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>("Failed to delete this person", HttpStatus.NOT_FOUND);
        }
    }

    }