package com.SafetyNet.SafetyNet.repository;

import com.SafetyNet.SafetyNet.model.MedicalRecord;

import java.util.List;

public interface MedicalRecordRepository {
    MedicalRecord findByFirstNameAndLastName(String firstName, String lastName);

    MedicalRecord findByFirstNameAndLastNameAndBirthdateAndMedicationsAndAllergies(
            String firstName, String lastName, String birthdate, List<String> medications, List<String> allergies
    );
}
