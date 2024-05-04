package com.SafetyNet.SafetyNet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SafetyNet.SafetyNet.dto.ChildInfo;
import com.SafetyNet.SafetyNet.service.contracts.IPersonService;

@RestController
public class ChildAlertController {

    @Autowired
    private IPersonService iPersonService;

    @GetMapping("/childAlert")
    public ResponseEntity<List<ChildInfo>> getChildAlert(@RequestParam String address) {
        List<ChildInfo> childrenInfo = iPersonService.getChildAlert(address);

        if (!childrenInfo.isEmpty()) {
            return new ResponseEntity<>(childrenInfo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}