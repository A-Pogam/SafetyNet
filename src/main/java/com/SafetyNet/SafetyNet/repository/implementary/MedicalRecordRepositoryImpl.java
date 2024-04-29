package com.SafetyNet.SafetyNet.repository.implementary;

import com.SafetyNet.SafetyNet.model.MedicalRecord;
import com.SafetyNet.SafetyNet.repository.MedicalRecordRepository;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class MedicalRecordRepositoryImpl implements MedicalRecordRepository {
    private final List<MedicalRecord> medicalRecords = new ArrayList<>();

    @Override
    public MedicalRecord findByFirstNameAndLastName(String firstName, String lastName) {
        for (MedicalRecord medicalRecord : medicalRecords) {
            if (medicalRecord.getFirstName().equals(firstName) && medicalRecord.getLastName().equals(lastName)) {
                return medicalRecord;
            }
        }
        return null;
    }

    @Override
    public MedicalRecord save(MedicalRecord medicalRecord) {
        medicalRecords.add(medicalRecord);
        return medicalRecord;
    }

    @Override
    public void deleteByFirstNameAndLastName(String firstName, String lastName) {
        Iterator<MedicalRecord> iterator = medicalRecords.iterator();
        while (iterator.hasNext()) {
            MedicalRecord medicalRecord = iterator.next();
            if (medicalRecord.getFirstName().equals(firstName) && medicalRecord.getLastName().equals(lastName)) {
                iterator.remove();
                break;
            }
        }
    }

    @Override
    public List<MedicalRecord> findAll() {
        return new ArrayList<>(medicalRecords);
    }

    @Override
    public MedicalRecord findByFirstNameAndLastNameAndBirthdateAndMedicationsAndAllergies(
            String firstName, String lastName, String birthdate, List<String> medications, List<String> allergies
    ) {
        for (MedicalRecord medicalRecord : medicalRecords) {
            if (medicalRecord.getFirstName().equals(firstName) && medicalRecord.getLastName().equals(lastName)
                    && medicalRecord.getBirthdate().equals(birthdate)
                    && medicalRecord.getMedications().containsAll(medications)
                    && medicalRecord.getAllergies().containsAll(allergies)) {
                return medicalRecord;
            }
        }
        return null;
    }
}

