package controller;

import model.MedicalRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import service.PersonService;
import model.Person;
import service.MedicalRecordService;
import service.FireStationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/person")
public class PersonController {
    private final PersonService personService;
    private final MedicalRecordService medicalRecordService;
    private final FireStationService fireStationService;


    public PersonController(PersonService personService, MedicalRecordService medicalRecordService, FireStationService fireStationService) {
        this.personService = personService;
        this.medicalRecordService = medicalRecordService;
        this.fireStationService = fireStationService;

    }


    @GetMapping
    public ResponseEntity<List<Person>> getAllPersons() {
        List<Person> persons = personService.getAllPersons();
        return ResponseEntity.ok(persons);
    }


    @PostMapping
    public ResponseEntity<String> addPerson(@RequestBody Person person) {
        Person addedPerson = personService.addPerson(person);
        if (addedPerson != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Person added successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add this person");
        }
    }


    @PutMapping("/{firstName}/{lastName}")
    public ResponseEntity<String> updatePerson(
            @PathVariable String firstName,
            @PathVariable String lastName,
            @RequestBody Person updatedPerson) {
        // Set the updated person's first name and last name
        updatedPerson.setFirstname(firstName);
        updatedPerson.setLastname(lastName);

        // Update the person using the service
        Person updated = personService.updatePerson(updatedPerson);

        if (updated != null) {
            return new ResponseEntity<>("Person updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to update this person", HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping("/{firstName}/{lastName}")
    public ResponseEntity<String> deletePerson(
            @PathVariable String firstName,
            @PathVariable String lastName) {
        boolean deleted = personService.deletePerson(firstName, lastName);
        if (deleted) {
            return new ResponseEntity<>("Person deleted successfully", HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>("Failed to delete this person", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/personInfo")
    public ResponseEntity<?> getPersonInfo(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) {
        Person person = personService.getPersonByName(firstName, lastName);

        if (person == null) {
            return ResponseEntity.notFound().build(); // Aucune personne trouvée
        }

        // Créer une carte pour stocker les détails de cette personne
        Map<String, Object> personDetails = new HashMap<>();
        personDetails.put("name", person.getFirstname() + " " + person.getLastname());
        personDetails.put("address", person.getAddress());
        personDetails.put("email", person.getEmail());

        // Récupérer le dossier médical de la personne
        MedicalRecord medicalRecord = medicalRecordService.getMedicalRecordByName(person.getFirstname(), person.getLastname());
        if (medicalRecord != null) {
            // Calculer l'âge à partir de la date de naissance
            int age = fireStationService.calculateAge(medicalRecord.getBirthdate());
            personDetails.put("age", age);

            // Ajouter les antécédents médicaux
            Map<String, Object> medicalHistory = new HashMap<>();
            medicalHistory.put("medications", medicalRecord.getMedications());
            medicalHistory.put("allergies", medicalRecord.getAllergies());
        }

        // Retourner les détails de la personne
        return ResponseEntity.ok(personDetails);
    }

}
