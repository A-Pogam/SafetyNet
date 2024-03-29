package controller;

import dto.ChildInfo;
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

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ChildAlertController {
    private static final Logger logger = LoggerFactory.getLogger(ChildAlertController.class);


    private final PersonService personService;
    private final FireStationService fireStationService;
    private final MedicalRecordService medicalRecordService;

    public ChildAlertController(PersonService personService, FireStationService fireStationService, MedicalRecordService medicalRecordService) {
        this.personService = personService;
        this.fireStationService = fireStationService;
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping("/childAlert")
    public ResponseEntity<List<ChildInfo>> getChildAlert(@RequestParam String address) {
        logger.info("Received request to get child alert for address: {}", address);
        List<Person> residents = personService.getPersonsByAddress(address);
        List<ChildInfo> children = residents.stream()
                .map(this::mapPersonToChildInfo)
                .filter(childInfo -> childInfo != null && childInfo.getAge() <= 18)
                .collect(Collectors.toList());
        logger.info("Found {} children in the household for address: {}", children.size(), address);
        return ResponseEntity.ok(children);
    }

    public ChildInfo mapPersonToChildInfo(Person person) {
        // Get medical record for the person
        MedicalRecord medicalRecord = medicalRecordService.getMedicalRecordByName(person.getFirstname(), person.getLastname());
        if (medicalRecord == null) {
            return null;
        }

        // Calculate age based on birthdate using FireStationService
        int age = fireStationService.calculateAge(medicalRecord.getBirthdate());
        if (age == -1) {
            return null;
        }

        // Get other members of the household
        List<Person> householdMembers = personService.getPersonsByAddress(person.getAddress())
                .stream()
                .filter(p -> !p.getFirstname().equals(person.getFirstname()) && !p.getLastname().equals(person.getLastname()))
                .collect(Collectors.toList());

        return new ChildInfo(person.getFirstname(), person.getLastname(), age, householdMembers);
    }
}


