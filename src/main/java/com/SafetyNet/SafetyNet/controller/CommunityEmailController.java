
package com.SafetyNet.SafetyNet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SafetyNet.SafetyNet.service.contracts.IPersonService;

@RestController
public class CommunityEmailController {

    @Autowired
    private IPersonService iPersonService;

    @GetMapping("/communityEmail")
    public ResponseEntity<List<String>> getEmailsByCity(@RequestParam("city") String city) {
        List<String> uniqueEmails = iPersonService.getEmailsByCity(city);

        if (!uniqueEmails.isEmpty()) {
            return new ResponseEntity<>(uniqueEmails, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
