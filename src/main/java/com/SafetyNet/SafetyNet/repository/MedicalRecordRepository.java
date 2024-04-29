package com.SafetyNet.SafetyNet.repository;

import com.SafetyNet.SafetyNet.model.MedicalRecord;

import java.util.List;

public interface MedicalRecordRepository {
    MedicalRecord findByFirstNameAndLastName(String firstName, String lastName);

    MedicalRecord save(MedicalRecord medicalRecord);

    void deleteByFirstNameAndLastName(String firstName, String lastName);

    List<MedicalRecord> findAll();

    MedicalRecord findByFirstNameAndLastNameAndBirthdateAndMedicationsAndAllergies(
            String firstName, String lastName, String birthdate, List<String> medications, List<String> allergies
    );
}
