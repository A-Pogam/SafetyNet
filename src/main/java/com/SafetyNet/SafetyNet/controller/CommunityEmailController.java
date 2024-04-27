package com.SafetyNet.SafetyNet.controller;

import com.SafetyNet.SafetyNet.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class CommunityEmailController {
    private static final Logger logger = LoggerFactory.getLogger(CommunityEmailController.class);


    private final PersonService personService;

    @Autowired
    public CommunityEmailController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/communityEmail")
    public ResponseEntity<List<String>> getEmailsByCity(@RequestParam("city") String city) {
        List<String> emails = personService.getEmailsByCity(city);
        if (emails != null) {
            Set<String> uniqueEmails = new HashSet<>(emails);
            List<String> uniqueEmailsList = uniqueEmails.stream().collect(Collectors.toList());
            return ResponseEntity.ok(uniqueEmailsList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

