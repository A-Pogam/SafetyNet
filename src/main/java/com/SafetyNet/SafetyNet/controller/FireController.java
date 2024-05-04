
package com.SafetyNet.SafetyNet.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SafetyNet.SafetyNet.service.contracts.IFireStationService;

@RestController
public class FireController {

    @Autowired
    private IFireStationService iFireStationService;

    @GetMapping("/fire")
    public ResponseEntity<Map<String, Object>> getResidentsAndFireStationByAddress(@RequestParam("address") String address) {
        Map<String, Object> residentsAndFireStations = iFireStationService.getResidentsAndFireStationByAddress(address);

        if (residentsAndFireStations != null) {
            return new ResponseEntity<>(residentsAndFireStations, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
