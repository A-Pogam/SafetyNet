package com.SafetyNet.SafetyNet.controller;

import com.SafetyNet.SafetyNet.dto.ChildInfo;
import com.SafetyNet.SafetyNet.model.MedicalRecord;
import com.SafetyNet.SafetyNet.model.Person;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.SafetyNet.SafetyNet.service.FireStationService;
import com.SafetyNet.SafetyNet.service.MedicalRecordService;
import com.SafetyNet.SafetyNet.service.PersonService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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

        // Récupérer la liste des résidents de l'adresse spécifiée
        List<Person> residents = personService.getPersonsByAddress(address);

        // Créer une liste pour stocker les informations sur les enfants
        List<ChildInfo> children = new ArrayList<>();

        // Parcourir la liste des résidents pour trouver les enfants
        for (Person resident : residents) {
            ChildInfo childInfo = personService.mapPersonToChildInfo(resident, fireStationService, medicalRecordService);
            if (childInfo != null && childInfo.getAge() <= 18) {
                // Récupérer les autres membres du foyer (enfants exclus)
                List<Person> householdMembers = residents.stream()
                        .filter(person -> !person.equals(resident))
                        .collect(Collectors.toList());
                // Ajouter les autres membres du foyer à l'objet ChildInfo
                childInfo.setHouseholdMembers(householdMembers);
                // Ajouter l'enfant à la liste des enfants
                children.add(childInfo);
            }
        }

        logger.info("Found {} children in the household for address: {}", children.size(), address);
        return ResponseEntity.ok(children);
    }

}


