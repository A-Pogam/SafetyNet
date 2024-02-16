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

import java.util.HashMap;
import java.util.Map;

@RestController
public class PersonInfoController {

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
