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
import org.springframework.web.bind.annotation.RestController;

import com.SafetyNet.SafetyNet.model.MedicalRecord;
import com.SafetyNet.SafetyNet.service.contracts.IMedicalRecordService;

@RestController

public class MedicalRecordController {

    @Autowired
    private IMedicalRecordService iMedicalRecordService;

    @GetMapping("/medicalRecords")
    public ResponseEntity<List<MedicalRecord>> getAllMedicalRecords() {
        List<MedicalRecord> medicalRecords = iMedicalRecordService.getAllMedicalRecords();

        if (!medicalRecords.isEmpty()) {
            return new ResponseEntity<>(medicalRecords, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/medicalRecord")
    public ResponseEntity<MedicalRecord> addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        MedicalRecord addedRecord = iMedicalRecordService.addMedicalRecord(medicalRecord);

        if (addedRecord != null) {
            return new ResponseEntity<>(addedRecord, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/medicalRecords/{firstName}/{lastName}")
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@PathVariable String firstName, @PathVariable String lastName, @RequestBody MedicalRecord medicalRecordUpdate) {
        MedicalRecord updatedMedicalRecord = iMedicalRecordService.updateMedicalRecord(firstName, lastName, medicalRecordUpdate);

        if (updatedMedicalRecord != null) {
            return new ResponseEntity<>(updatedMedicalRecord, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/medicalRecords/{firstName}/{lastName}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable String firstName, @PathVariable String lastName) {
        boolean deleted = iMedicalRecordService.deleteMedicalRecord(firstName, lastName);

        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}