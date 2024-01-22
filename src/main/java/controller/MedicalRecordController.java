package controller;

import model.MedicalRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import service.MedicalRecordService;

import java.util.List;

@Controller
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @Autowired
    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @PostMapping
    public ResponseEntity<MedicalRecord> addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        MedicalRecord addedRecord = medicalRecordService.addMedicalRecord(medicalRecord);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedRecord);
    }

    @PutMapping("/{firstName}/{lastName}")
    public ResponseEntity<MedicalRecord> updateMedicalRecord(
            @PathVariable String firstName,
            @PathVariable String lastName,
            @RequestParam String birthdate,
            @RequestParam List<String> medications,
            @RequestParam List<String> allergies,
            @RequestBody MedicalRecord medicalRecord
    ) {
        medicalRecord.setBirthdate(birthdate);
        medicalRecord.setMedications(medications);
        medicalRecord.setAllergies(allergies);

        MedicalRecord updatedRecord = medicalRecordService.updateMedicalRecord(
                firstName, lastName, birthdate, medications, allergies, medicalRecord
        );

        if (updatedRecord != null) {
            return ResponseEntity.ok(updatedRecord);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{firstName}/{lastName}")
    public ResponseEntity<Void> deleteMedicalRecord(
            @PathVariable String firstName,
            @PathVariable String lastName,
            @RequestParam String birthdate,
            @RequestParam List<String> medications,
            @RequestParam List<String> allergies
    ) {
        boolean deleted = medicalRecordService.deleteMedicalRecord(firstName, lastName, birthdate, medications, allergies);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
