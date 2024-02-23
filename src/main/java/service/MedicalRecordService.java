package service;

import model.MedicalRecord;
import model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.MedicalRecordRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Service
public class MedicalRecordService {
    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordService.class);


    private final List<MedicalRecord> medicalRecords = new ArrayList<>();

    public List<MedicalRecord> getMedicalRecords() {
        logger.info("Retrieving all medical records");
        return new ArrayList<>(medicalRecords);
    }

    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
        logger.info("Adding medical record: {}", medicalRecord);
        medicalRecords.add(medicalRecord);
        logger.info("Medical record added successfully");
        return medicalRecord;
    }

    public MedicalRecord updateMedicalRecord(String firstName, String lastName, MedicalRecord updatedMedicalRecord) {
        logger.info("Updating medical record for person with name: {}, {}", firstName, lastName);
        for (MedicalRecord existingRecord : medicalRecords) {
            if (existingRecord.getFirstName().equals(firstName) && existingRecord.getLastName().equals(lastName)) {
                existingRecord.setBirthdate(updatedMedicalRecord.getBirthdate());
                existingRecord.setMedications(updatedMedicalRecord.getMedications());
                existingRecord.setAllergies(updatedMedicalRecord.getAllergies());
                logger.info("Medical record updated successfully");
                return existingRecord;
            }
        }
        logger.warn("No medical record found for update for person with name: {}, {}", firstName, lastName);
        return null;
    }

    public boolean deleteMedicalRecord(String firstName, String lastName) {
        logger.info("Deleting medical record for person with name: {}, {}", firstName, lastName);
        Iterator<MedicalRecord> iterator = medicalRecords.iterator();
        while (iterator.hasNext()) {
            MedicalRecord existingRecord = iterator.next();
            if (existingRecord.getFirstName().equals(firstName) && existingRecord.getLastName().equals(lastName)) {
                iterator.remove();
                logger.info("Medical record deleted successfully");
                return true;
            }
        }
        logger.warn("No medical record found for deletion for person with name: {}, {}", firstName, lastName);
        return false;
    }

    public List<MedicalRecord> getAllMedicalRecords() {
        logger.info("Retrieving all medical records");
        return medicalRecords;
    }


    public MedicalRecord getMedicalRecordByName(String firstName, String lastName) {
        logger.info("Retrieving medical record for person with name: {}, {}", firstName, lastName);
        return medicalRecords.stream()
                .filter(record -> record.getFirstName().equals(firstName) && record.getLastName().equals(lastName))
                .findFirst()
                .orElse(null);
    }
}