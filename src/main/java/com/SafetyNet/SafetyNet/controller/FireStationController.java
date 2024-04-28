package com.SafetyNet.SafetyNet.controller;

import com.SafetyNet.SafetyNet.dto.FireStationCoverage;
import com.SafetyNet.SafetyNet.model.FireStation;
import com.SafetyNet.SafetyNet.service.FireStationService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class FireStationController {

    private final FireStationService fireStationService;

    public FireStationController(FireStationService fireStationService) {
        this.fireStationService = fireStationService;
    }

    @GetMapping("/firestations")
    public ResponseEntity<List<FireStation>> getAllFireStations() {
        return ResponseEntity.ok(fireStationService.getAllFireStations());
    }

    @PostMapping("/firestations")
    public ResponseEntity<FireStation> addMapping(@RequestBody FireStation fireStation) {
        return ResponseEntity.ok(fireStationService.addMapping(fireStation));
    }

    @PutMapping("/firestations/{address}")
    public ResponseEntity<FireStation> updateFireStationNumber(
            @PathVariable String address,
            @RequestParam int stationNumber
    ) {
        FireStation updatedMapping = fireStationService.updateFireStationNumber(address, stationNumber);
        return updatedMapping != null ? ResponseEntity.ok(updatedMapping) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/firestations/{address}")
    public ResponseEntity<Void> deleteMapping(@PathVariable String address) {
        return fireStationService.deleteMapping(address) ?
                ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/firestation")
    public ResponseEntity<FireStationCoverage> getFireStationCoverage(@RequestParam("stationNumber") int stationNumber) {
        FireStationCoverage coverage = fireStationService.getCoverageByStationNumber(stationNumber);
        return coverage != null ? ResponseEntity.ok(coverage) : ResponseEntity.notFound().build();
    }

    @GetMapping("/fire")
    public ResponseEntity<?> getResidentsAndFireStation(@RequestParam("address") String address) {
        Map<String, Object> response = fireStationService.getResidentsAndFireStation(address);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    @GetMapping("/flood/stations")
    public ResponseEntity<?> getFloodStations(@RequestParam("stations") List<Integer> stationNumbers) {
        List<FireStation> floodStations = fireStationService.getFloodStationsAsFireStations(stationNumbers);
        return ResponseEntity.ok(floodStations);
    }


}
