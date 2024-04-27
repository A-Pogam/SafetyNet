package com.SafetyNet.SafetyNet.controller;

import com.SafetyNet.SafetyNet.model.MedicalRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.SafetyNet.SafetyNet.service.MedicalRecordService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordController.class);

    private final MedicalRecordService medicalRecordService;

    @Autowired
    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<MedicalRecord>> getAllMedicalRecord() {
        logger.info("Received request to get all medical records.");
        List<MedicalRecord> medicalRecords = medicalRecordService.getAllMedicalRecords();
        logger.info("Retrieved {} medical records.", medicalRecords.size());
        return ResponseEntity.ok(medicalRecords);
    }

    @PostMapping
    public ResponseEntity<MedicalRecord> addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        logger.info("Received request to add medical record for {} {}.", medicalRecord.getFirstName(), medicalRecord.getLastName());
        MedicalRecord addedRecord = medicalRecordService.addMedicalRecord(medicalRecord);
        logger.info("Added medical record for {} {}.", medicalRecord.getFirstName(), medicalRecord.getLastName());
        return ResponseEntity.status(HttpStatus.CREATED).body(addedRecord);
    }

    @PutMapping("/{firstName}/{lastName}")
    public ResponseEntity<MedicalRecord> updateMedicalRecord(
            @PathVariable String firstName,
            @PathVariable String lastName,
            @RequestBody MedicalRecord updatedMedicalRecord
    ) {
        logger.info("Received request to update medical record for {} {}.", firstName, lastName);
        MedicalRecord updatedRecord = medicalRecordService.updateMedicalRecord(firstName, lastName, updatedMedicalRecord);

        if (updatedRecord != null) {
            logger.info("Updated medical record for {} {}.", firstName, lastName);
            return ResponseEntity.ok(updatedRecord);
        } else {
            logger.error("Failed to update medical record for {} {}. Record not found.", firstName, lastName);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{firstName}/{lastName}")
    public ResponseEntity<Void> deleteMedicalRecord(
            @PathVariable String firstName,
            @PathVariable String lastName
    ) {
        logger.info("Received request to delete medical record for {} {}.", firstName, lastName);
        boolean deleted = medicalRecordService.deleteMedicalRecord(firstName, lastName);
        if (deleted) {
            logger.info("Deleted medical record for {} {}.", firstName, lastName);
            return ResponseEntity.noContent().build();
        } else {
            logger.error("Failed to delete medical record for {} {}. Record not found.", firstName, lastName);
            return ResponseEntity.notFound().build();
        }
    }

}
