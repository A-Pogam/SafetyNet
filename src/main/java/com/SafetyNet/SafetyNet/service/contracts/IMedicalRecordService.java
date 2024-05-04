
package com.SafetyNet.SafetyNet.service.contracts;

import java.time.LocalDate;
import java.util.List;

import com.SafetyNet.SafetyNet.model.MedicalRecord;

public interface IMedicalRecordService {

    public List<MedicalRecord> getAllMedicalRecords();

    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord);
    public MedicalRecord updateMedicalRecord(String firstName, String lastName, MedicalRecord medicalRecordUpdate);

    public boolean deleteMedicalRecord(String firstName, String lastName);

    public int calculateAge(LocalDate birthdate);
}
