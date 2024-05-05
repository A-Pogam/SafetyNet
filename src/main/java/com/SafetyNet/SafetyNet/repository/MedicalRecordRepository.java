package com.SafetyNet.SafetyNet.repository;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.SafetyNet.SafetyNet.model.MedicalRecord;
import com.SafetyNet.SafetyNet.repository.contracts.IMedicalRecordRepository;

@Repository
public class MedicalRecordRepository implements IMedicalRecordRepository {

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordRepository.class);

    private List<MedicalRecord> medicalRecords = new ArrayList<>();

    @Override
    public List<MedicalRecord> findAll() {
        logger.debug("Searching for all medical records.");
        return new ArrayList<>(medicalRecords);
    }

    @Override
    public MedicalRecord findByFirstNameAndLastName(String firstName, String lastName) {
        logger.debug("Searching for for medical record with name: {} {}.", firstName, lastName);
        return medicalRecords.stream()
                .filter(medicalRecord -> medicalRecord.getFirstName().equals(firstName) && medicalRecord.getLastName().equals(lastName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public MedicalRecord save(MedicalRecord medicalRecord) {
        logger.debug("Adding medical record: {} {}.", medicalRecord.getFirstName(), medicalRecord.getLastName());
        medicalRecords.add(medicalRecord);
        logger.info("Medical record added successfully.");

        return medicalRecord;
    }

    @Override
    public MedicalRecord update(MedicalRecord existingMedicalRecord, MedicalRecord medicalRecordUpdate) {
        logger.debug("Updating medical record for person with name: {} {}.", existingMedicalRecord.getFirstName(), existingMedicalRecord.getLastName());

        if (medicalRecordUpdate.getFirstName() != null) {
            existingMedicalRecord.setFirstName(medicalRecordUpdate.getFirstName());
        }
        if (medicalRecordUpdate.getLastName() != null) {
            existingMedicalRecord.setLastName(medicalRecordUpdate.getLastName());
        }
        if (medicalRecordUpdate.getBirthdate() != null) {
            existingMedicalRecord.setBirthdate(medicalRecordUpdate.getBirthdate());
        }
        if (medicalRecordUpdate.getMedications() != null) {
            existingMedicalRecord.setMedications(medicalRecordUpdate.getMedications());
        }
        if (medicalRecordUpdate.getAllergies() != null) {
            existingMedicalRecord.setAllergies(medicalRecordUpdate.getAllergies());
        }

        logger.info("Medical record updated successfully.");
        return existingMedicalRecord;
    }

    @Override
    public void deleteByFirstNameAndLastName(String firstName, String lastName) {
        // Implémentation de la suppression d'une personne par prénom et nom
        logger.debug("Deleting medical record with name: {} {}.", firstName, lastName);
        medicalRecords.removeIf(medicalRecord -> medicalRecord.getFirstName().equals(firstName) && medicalRecord.getLastName().equals(lastName));
        logger.info("Medical record deleted successfully.");
    }
}