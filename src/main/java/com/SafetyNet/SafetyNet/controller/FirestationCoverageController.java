
package com.SafetyNet.SafetyNet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SafetyNet.SafetyNet.dto.FireStationCoverage;
import com.SafetyNet.SafetyNet.service.contracts.IFireStationService;

@RestController
public class FirestationCoverageController {

    @Autowired
    private IFireStationService iFireStationService;

    @GetMapping("/firestation")
    public ResponseEntity<FireStationCoverage> getFireStationCoverage(@RequestParam("stationNumber") int stationNumber) {
        FireStationCoverage fireStationCoverage = iFireStationService.getCoverageByStationNumber(stationNumber);

        if (!fireStationCoverage.getCoveragePeople().isEmpty()) {
            return new ResponseEntity<>(fireStationCoverage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
