package com.SafetyNet.SafetyNet.controller;

import com.SafetyNet.SafetyNet.dto.FireStationCoverage;
import com.SafetyNet.SafetyNet.model.MedicalRecord;
import com.SafetyNet.SafetyNet.model.Person;
import com.SafetyNet.SafetyNet.service.FireStationService;
import com.SafetyNet.SafetyNet.service.MedicalRecordService;
import com.SafetyNet.SafetyNet.service.PersonService;
import com.SafetyNet.SafetyNet.model.FireStation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@RestController //no need of ResponseBody with RestController
public class FireStationController {

    private static final Logger logger = LoggerFactory.getLogger(FireStationController.class);

    private final FireStationService fireStationService;
    private final PersonService personService;
    private final MedicalRecordService medicalRecordService;
    private List<FireStation> fireStations;





    public FireStationController(FireStationService fireStationService, PersonService personService, MedicalRecordService medicalRecordService,  List<FireStation> fireStations) {
        this.fireStationService = fireStationService;
        this.personService = personService;
        this.medicalRecordService = medicalRecordService;
        this.fireStations = fireStations != null ? fireStations : new ArrayList<>();
    }


    @GetMapping("/firestations")
    public ResponseEntity<List<FireStation>> getAllFireStations() {
        logger.info("Received request to get all fire stations.");
        List<FireStation> fireStations = fireStationService.getAllFireStations();
        logger.info("Retrieved {} fire stations.", fireStations.size());
        return ResponseEntity.ok(fireStations);
    }


    @PostMapping
    public ResponseEntity<FireStation> addMapping(@RequestBody FireStation fireStation) {
        logger.info("Received request to add fire station mapping: {}", fireStation);
        FireStation addedMapping = fireStationService.addMapping(fireStation);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedMapping);
    }


    @PutMapping("/{address}")
    public ResponseEntity<FireStation> updateFireStationNumber(
            @PathVariable String address,
            @RequestParam int stationNumber
    ) {
        logger.info("Received request to update fire station number for address: {}, new station number: {}", address, stationNumber);
        FireStation updatedMapping = fireStationService.updateFireStationNumber(address, stationNumber);
        if (updatedMapping != null) {
            logger.info("Fire station number updated successfully for address: {}", address);
            return ResponseEntity.ok(updatedMapping);
        } else {
            logger.error("Failed to update fire station number for address: {}", address);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{address}")
    public ResponseEntity<Void> deleteMapping(@PathVariable String address) {
        logger.info("Received request to delete fire station mapping for address: {}", address);

        // Vérifier si fireStations est null et l'initialiser si nécessaire
        if (fireStations == null) {
            fireStations = new ArrayList<>();
        }

        boolean deleted = fireStationService.deleteMapping(address); // Appel du service pour supprimer la station de pompiers

        // Gestion de la réponse en fonction du résultat de la suppression
        if (deleted) {
            logger.info("Fire station mapping deleted successfully for address: {}", address);
            return ResponseEntity.noContent().build(); // Réponse HTTP 204 (No Content)
        } else {
            logger.error("Failed to delete fire station mapping for address: {}", address);
            return ResponseEntity.notFound().build(); // Réponse HTTP 404 (Not Found)
        }
    }

    @GetMapping("/delete/{address}")
    public String showDeleteForm(@PathVariable String address, Model model) {
        FireStation fireStation = new FireStation();
        fireStation.setAddress(address);
        model.addAttribute("fireStation", fireStation);
        return "firestation/deleteMapping";
    }

    @GetMapping("/firestation")
    public ResponseEntity<FireStationCoverage> getFireStationCoverage(@RequestParam("stationNumber") int stationNumber) {
        logger.info("Received request to get fire station coverage for station number: {}", stationNumber);
        FireStationCoverage coverage = fireStationService.getCoverageByStationNumber(stationNumber);
        if (coverage != null) {
            logger.info("Retrieved fire station coverage for station number: {}", stationNumber);
            return ResponseEntity.ok(coverage);
        } else {
            logger.error("Failed to retrieve fire station coverage for station number: {}", stationNumber);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/fire")
    public ResponseEntity<?> getResidentsAndFireStation(@RequestParam("address") String address) {
        logger.info("Received request to get residents and fire station for address: {}", address);

        // Récupérer le numéro de la caserne desservant l'adresse donnée
        Integer fireStationNumber = fireStationService.getFireStationNumberByAddress(address);

        if (fireStationNumber == null) {
            return ResponseEntity.notFound().build(); // Retourne une réponse 404 si l'adresse n'est pas trouvée
        }

        // Récupérer les habitants vivant à l'adresse donnée
        List<Person> residents = personService.getPersonsByAddress(address);

        // Construire la réponse avec les informations des résidents et le numéro de la caserne
        Map<String, Object> response = new HashMap<>();
        response.put("fireStationNumber", fireStationNumber);
        response.put("residents", residents);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/flood/stations")
    public ResponseEntity<?> getFloodStations(@RequestParam("stations") List<Integer> stationNumbers) {
        logger.info("Received request to get flood stations for station numbers: {}", stationNumbers);

        // Créer une liste pour stocker les détails des foyers desservis par chaque caserne
        List<Map<String, Object>> floodStationsDetails = new ArrayList<>();

        // Pour chaque numéro de caserne spécifié
        for (Integer stationNumber : stationNumbers) {
            // Convertir l'entier en une liste contenant un seul élément
            List<Integer> singleStationNumberList = Collections.singletonList(stationNumber);

            // Récupérer les casernes de pompiers desservant ce numéro de caserne
            List<FireStation> floodStations = fireStationService.getFloodStations(singleStationNumberList);

            // Pour chaque caserne de pompiers
            for (FireStation floodStation : floodStations) {
                // Récupérer l'adresse desservie par cette caserne
                String address = floodStation.getAddress();

                // Récupérer les personnes vivant à cette adresse
                List<Person> residents = personService.getPersonsByAddress(address);

                // Créer une liste pour stocker les détails des résidents de cette adresse
                List<Map<String, Object>> residentsDetails = new ArrayList<>();

                // Pour chaque résident
                for (Person resident : residents) {
                    // Créer une carte pour stocker les détails de ce résident
                    Map<String, Object> residentDetail = new HashMap<>();
                    residentDetail.put("name", resident.getFirstname() + " " + resident.getLastname());
                    residentDetail.put("phone", resident.getPhone());

                    // Récupérer le dossier médical de la personne
                    MedicalRecord medicalRecord = medicalRecordService.getMedicalRecordByName(resident.getFirstname(), resident.getLastname());
                    if (medicalRecord != null) {
                        // Calculer l'âge à partir de la date de naissance
                        int age = fireStationService.calculateAge(medicalRecord.getBirthdate());
                        residentDetail.put("age", age);

                        // Ajouter les médicaments et allergies
                        residentDetail.put("medications", medicalRecord.getMedications());
                        residentDetail.put("allergies", medicalRecord.getAllergies());
                    }

                    // Ajouter les détails de ce résident à la liste
                    residentsDetails.add(residentDetail);
                }

                // Créer une carte pour stocker les détails de cette adresse avec les résidents
                Map<String, Object> addressDetails = new HashMap<>();
                addressDetails.put("address", address);
                addressDetails.put("residents", residentsDetails);

                floodStationsDetails.add(addressDetails);
            }
        }

        // Retourner la liste des foyers desservis par les casernes de pompiers spécifiées
        return ResponseEntity.ok(floodStationsDetails);
    }

}
