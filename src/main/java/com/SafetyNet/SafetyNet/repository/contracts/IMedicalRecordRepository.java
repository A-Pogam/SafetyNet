package com.SafetyNet.SafetyNet.repository.contracts;

import com.SafetyNet.SafetyNet.model.MedicalRecord;

import java.util.List;

public interface IMedicalRecordRepository {

    List<MedicalRecord> findAll();
    MedicalRecord findByFirstNameAndLastName(String firstName, String lastName);
    //MedicalRecord findByFirstNameAndLastNameAndBirthdateAndMedicationsAndAllergies(String firstName, String lastName, String birthdate, List<String> medications, List<String> allergies);

    MedicalRecord save(MedicalRecord medicalRecord);
    MedicalRecord update(MedicalRecord existingMedicalRecord, MedicalRecord medicalRecordUpdate);

    void deleteByFirstNameAndLastName(String firstName, String lastName);
}