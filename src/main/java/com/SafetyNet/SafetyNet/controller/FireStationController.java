
package com.SafetyNet.SafetyNet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SafetyNet.SafetyNet.model.FireStation;
import com.SafetyNet.SafetyNet.service.contracts.IFireStationService;

@RestController
public class FireStationController {

    @Autowired
    private IFireStationService iFireStationService;

    @GetMapping("/firestations")
    public ResponseEntity<List<FireStation>> getAllFireStations() {
        List<FireStation> firestations = iFireStationService.getAllFireStations();

        if (!firestations.isEmpty()) {
            return new ResponseEntity<>(firestations, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/firestation")
    public ResponseEntity<FireStation> addMapping(@RequestBody FireStation fireStation) {
        FireStation addedFirestation = iFireStationService.addMapping(fireStation);

        if (addedFirestation != null) {
            return new ResponseEntity<>(addedFirestation, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/firestations/{address}/{stationNumber}")
    public ResponseEntity<FireStation> updateFireStationNumber(@PathVariable String address, @PathVariable int stationNumber, @RequestParam int newStationNumber) {
        FireStation updatedMapping = iFireStationService.updateFireStationNumber(address, stationNumber, newStationNumber);

        if (updatedMapping != null) {
            return new ResponseEntity<>(updatedMapping, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/firestations/{address}/{stationNumber}")
    public ResponseEntity<Void> deleteMapping(@PathVariable String address, @PathVariable int stationNumber) {
        boolean deleted = iFireStationService.deleteMapping(address, stationNumber);

        if (deleted) {
            return new ResponseEntity<>( HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
