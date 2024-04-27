package com.SafetyNet.SafetyNet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.SafetyNet.SafetyNet.service.FireStationService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class PhoneAlertController {
    private static final Logger logger = LoggerFactory.getLogger(PhoneAlertController.class);


    private final FireStationService fireStationService;

    public PhoneAlertController(FireStationService fireStationService) {
        this.fireStationService = fireStationService;
    }

    @GetMapping("/phoneAlert")
    public ResponseEntity<List<String>> getPhoneAlert(@RequestParam("firestation") List<Integer> firestations) {
        logger.info("Received request to get phone numbers for fire stations: {}", firestations);
        List<String> phoneNumbers = fireStationService.getPhoneNumbersServedByFireStations(firestations);
        if (phoneNumbers.isEmpty()) {
            logger.warn("No phone numbers found for the provided fire stations: {}", firestations);
            return ResponseEntity.notFound().build(); // Retourne 404 si aucun numéro n'est trouvé
        }
        logger.info("Retrieved phone numbers for fire stations: {}", firestations);
        return ResponseEntity.ok(phoneNumbers);
    }

}
