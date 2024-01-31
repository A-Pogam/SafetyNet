package service;

import model.MedicalRecord;
import model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.MedicalRecordRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Service
public class MedicalRecordService {

    private final List<MedicalRecord> medicalRecords = new ArrayList<>();

    public List<MedicalRecord> getMedicalRecords() {
        return new ArrayList<>(medicalRecords);
    }

    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
        medicalRecords.add(medicalRecord);
        return medicalRecord;
    }

    public MedicalRecord updateMedicalRecord(String firstName, String lastName, MedicalRecord updatedMedicalRecord) {
        for (MedicalRecord existingRecord : medicalRecords) {
            if (existingRecord.getFirstName().equals(firstName) && existingRecord.getLastName().equals(lastName)) {
                existingRecord.setBirthdate(updatedMedicalRecord.getBirthdate());
                existingRecord.setMedications(updatedMedicalRecord.getMedications());
                existingRecord.setAllergies(updatedMedicalRecord.getAllergies());
                return existingRecord;
            }
        }
        return null;
    }

    public boolean deleteMedicalRecord(String firstName, String lastName) {
        Iterator<MedicalRecord> iterator = medicalRecords.iterator();
        while (iterator.hasNext()) {
            MedicalRecord existingRecord = iterator.next();
            if (existingRecord.getFirstName().equals(firstName) && existingRecord.getLastName().equals(lastName)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecords;
    }
}