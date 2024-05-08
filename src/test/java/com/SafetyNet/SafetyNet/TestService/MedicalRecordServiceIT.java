package com.SafetyNet.SafetyNet.TestService;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.SafetyNet.SafetyNet.model.MedicalRecord;
import com.SafetyNet.SafetyNet.service.contracts.IMedicalRecordService;

@SpringBootTest
@AutoConfigureMockMvc
public class MedicalRecordServiceIT {

    @Autowired
    private IMedicalRecordService iMedicalRecordService;

    @Test
    public void getAllMedicalRecords_returnMedicalRecords() {
        List<MedicalRecord> medicalRecords = iMedicalRecordService.getAllMedicalRecords();

        assertThat(medicalRecords).isNotEmpty();
        assertThat(medicalRecords.getFirst().getFirstName()).isEqualTo("John");
        assertThat(medicalRecords.getFirst().getLastName()).isEqualTo("Boyd");
        assertThat(medicalRecords.getFirst().getBirthdate()).isEqualTo(LocalDate.parse("1984-03-06"));
        assertThat(medicalRecords.getFirst().getMedications()).isEqualTo(new ArrayList<>(Arrays.asList("aznol:350mg", "hydrapermazol:100mg")));
        assertThat(medicalRecords.getFirst().getAllergies()).isEqualTo(Arrays.asList("nillacilan"));
    }

    @Test
    public void addMedicalRecord_returnMedicalRecord() {
        MedicalRecord newMedicalRecord = new MedicalRecord("John", "Doe", LocalDate.parse("2001-01-20"), new ArrayList<>(Arrays.asList("tradoxidine:400mg")), new ArrayList<>(Arrays.asList("nillacilan")));

        MedicalRecord medicalRecordAdded = iMedicalRecordService.addMedicalRecord(newMedicalRecord);

        assertThat(medicalRecordAdded.getFirstName()).isEqualTo(newMedicalRecord.getFirstName());
        assertThat(medicalRecordAdded.getLastName()).isEqualTo(newMedicalRecord.getLastName());
        assertThat(medicalRecordAdded.getBirthdate()).isEqualTo(newMedicalRecord.getBirthdate());
        assertThat(medicalRecordAdded.getMedications()).isEqualTo(newMedicalRecord.getMedications());
        assertThat(medicalRecordAdded.getAllergies()).isEqualTo(newMedicalRecord.getAllergies());
    }

    @Test
    public void addMedicalRecord_returnNull() {
        MedicalRecord newMedicalRecord = new MedicalRecord("Eric", "Cadigan", LocalDate.parse("1945-08-06"), new ArrayList<>(Arrays.asList("tradoxidine:400mg")), new ArrayList<>());

        MedicalRecord medicalRecordAdded = iMedicalRecordService.addMedicalRecord(newMedicalRecord);

        assertThat(medicalRecordAdded).isNull();
    }

    @Test
    public void updateMedicalRecord_returnMedicalRecord() {
        MedicalRecord medicalRecordToUpdate = new MedicalRecord(null, null, LocalDate.parse("2001-10-07"), new ArrayList<>(Arrays.asList("terazine:10mg", "noznazol:250mg")), new ArrayList<>());

        MedicalRecord medicalRecordUpdated = iMedicalRecordService.updateMedicalRecord("Peter", "Duncan", medicalRecordToUpdate);

        assertThat(medicalRecordUpdated.getFirstName()).isEqualTo("Peter");
        assertThat(medicalRecordUpdated.getLastName()).isEqualTo("Duncan");
        assertThat(medicalRecordUpdated.getBirthdate()).isEqualTo(medicalRecordToUpdate.getBirthdate());
        assertThat(medicalRecordUpdated.getMedications()).isEqualTo(medicalRecordToUpdate.getMedications());
        assertThat(medicalRecordUpdated.getAllergies()).isEqualTo(medicalRecordToUpdate.getAllergies());
    }

    @Test
    public void updateMedicalRecord_returnNull() {
        MedicalRecord medicalRecordToUpdate = new MedicalRecord();

        MedicalRecord medicalRecordUpdated = iMedicalRecordService.updateMedicalRecord("John", "Cadigan", medicalRecordToUpdate);

        assertThat(medicalRecordUpdated).isNull();
    }

    @Test
    public void deleteMedicalRecord_returnTrue() {
        boolean isDeleted = iMedicalRecordService.deleteMedicalRecord("Jonanathan", "Marrack");
        assertThat(isDeleted).isTrue();
    }

    @Test
    public void deleteMedicalRecord_returnFalse() {
        boolean isDeleted = iMedicalRecordService.deleteMedicalRecord("John", "Cadigan");
        assertThat(isDeleted).isFalse();
    }

    @Test
    public void calculateAge_returnInt() {
        int age = iMedicalRecordService.calculateAge(LocalDate.parse("2000-01-01"));
        assertThat(age).isPositive();
    }
}