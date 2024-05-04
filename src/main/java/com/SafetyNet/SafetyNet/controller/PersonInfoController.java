package com.SafetyNet.SafetyNet.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SafetyNet.SafetyNet.service.contracts.IPersonService;

@RestController
public class PersonInfoController {

    @Autowired
    private IPersonService iPersonService;

    @GetMapping("/personInfo")
    public ResponseEntity<Map<String, Object>> getPersonInfo(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) {
        Map<String, Object> personDetails = iPersonService.getPersonInfo(firstName, lastName);

        if (personDetails != null) {
            return new ResponseEntity<>(personDetails, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}