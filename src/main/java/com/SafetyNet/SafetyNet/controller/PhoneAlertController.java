package com.SafetyNet.SafetyNet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SafetyNet.SafetyNet.service.contracts.IFireStationService;

@RestController
public class PhoneAlertController {

    @Autowired
    private IFireStationService iFireStationService;

    @GetMapping("/phoneAlert")
    public ResponseEntity<List<String>> getPhoneAlert(@RequestParam("firestation") int stationNumber) {
        List<String> phoneNumbers = iFireStationService.getPhoneNumbersServedByFireStation(stationNumber);

        if (!phoneNumbers.isEmpty()) {
            return new ResponseEntity<>(phoneNumbers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}