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

    public MedicalRecord updateMedicalRecord(MedicalRecord updatedMedicalRecord) {
        MedicalRecord existingRecord = medicalRecordRepository.findByFirstNameAndLastNameAndBirthdateAndMedicationsAndAllergies(
                updatedMedicalRecord.getFirstName(),
                updatedMedicalRecord.getLastName(),
                updatedMedicalRecord.getBirthdate(),
                updatedMedicalRecord.getMedications(),
                updatedMedicalRecord.getAllergies()
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

    public boolean deleteMedicalRecord(String firstName, String lastName, String birthdate, List<String> medications, List<String> allergies) {
        MedicalRecord existingRecord = medicalRecordRepository.findByFirstNameAndLastNameAndBirthdateAndMedicationsAndAllergies(
                firstName, lastName, birthdate, medications, allergies
        );

        if (existingRecord != null) {
            medicalRecordRepository.delete(existingRecord);
            return true;
        } else {
            return false;
        }
    }
}
