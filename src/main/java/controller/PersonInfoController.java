package controller;

import model.MedicalRecord;
import model.Person;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.FireStationService;
import service.MedicalRecordService;
import service.PersonService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@RestController
public class PersonInfoController {
    private static final Logger logger = LoggerFactory.getLogger(PersonInfoController.class);


    private final PersonService personService;
    private final MedicalRecordService medicalRecordService;
    private final FireStationService fireStationService;


    public PersonInfoController(PersonService personService, MedicalRecordService medicalRecordService, FireStationService fireStationService) {
        this.personService = personService;
        this.medicalRecordService = medicalRecordService;
        this.fireStationService = fireStationService;

    }

    @GetMapping("/personInfo")
    public ResponseEntity<?> getPersonInfo(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) {
        logger.info("Received request to get person info: {} {}", firstName, lastName);
        Person person = personService.getPersonByName(firstName, lastName);

        if (person == null) {
            logger.error("Person not found: {} {}", firstName, lastName);
            return ResponseEntity.notFound().build(); // Aucune personne trouvée
        }

        // Créer une carte pour stocker les détails de cette personne
        Map<String, Object> personDetails = new HashMap<>();
        personDetails.put("name", person.getFirstname() + " " + person.getLastname());
        personDetails.put("address", person.getAddress());
        personDetails.put("email", person.getEmail());
        personDetails.put("phone", person.getPhone());

        // Récupérer le dossier médical de la personne
        MedicalRecord medicalRecord = medicalRecordService.getMedicalRecordByName(person.getFirstname(), person.getLastname());
        if (medicalRecord != null) {
            // Calculer l'âge à partir de la date de naissance
            int age = fireStationService.calculateAge(medicalRecord.getBirthdate());
            personDetails.put("age", age);

            // Convertir les médicaments en une chaîne de caractères séparée par des virgules
            String medications = String.join(", ", medicalRecord.getMedications());
            personDetails.put("medications", medications);

            // Convertir les allergies en une chaîne de caractères séparée par des virgules
            String allergies = String.join(", ", medicalRecord.getAllergies());
            personDetails.put("allergies", allergies);
        }

        // Retourner les détails de la personne
        return ResponseEntity.ok(personDetails);
    }
}