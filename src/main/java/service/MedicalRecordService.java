package service;

import model.MedicalRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.MedicalRecordRepository;

import java.util.List;


@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;

    @Autowired
    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
        return medicalRecordRepository.save(medicalRecord);
    }

    public MedicalRecord updateMedicalRecord(String firstName, String lastName, MedicalRecord updatedMedicalRecord) {
        MedicalRecord existingRecord = medicalRecordRepository.findByFirstNameAndLastName(
                firstName, lastName
        );

        if (existingRecord != null) {
            existingRecord.setBirthdate(updatedMedicalRecord.getBirthdate());
            existingRecord.setMedications(updatedMedicalRecord.getMedications());
            existingRecord.setAllergies(updatedMedicalRecord.getAllergies());
            return medicalRecordRepository.save(existingRecord);
        } else {
            return null;
        }
    }

    public boolean deleteMedicalRecord(String firstName, String lastName) {
        MedicalRecord existingRecord = medicalRecordRepository.findByFirstNameAndLastName(
                firstName, lastName
        );

        if (existingRecord != null) {
            medicalRecordRepository.delete(existingRecord);
            return true;
        } else {
            return false;
        }
    }
}
