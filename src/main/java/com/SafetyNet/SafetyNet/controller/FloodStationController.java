package com.SafetyNet.SafetyNet.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SafetyNet.SafetyNet.service.contracts.IFireStationService;

@RestController
public class FloodStationController {

    @Autowired
    private IFireStationService iFireStationService;

    @GetMapping("/flood/stations")
    public ResponseEntity<List<Map<String, Object>>> getFloodStations(@RequestParam("stations") List<Integer> stationNumbers) {
        List<Map<String, Object>> floodStations = iFireStationService.getFloodStations(stationNumbers);

        if (!floodStations.isEmpty()) {
            return new ResponseEntity<>(floodStations, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}