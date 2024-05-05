package com.SafetyNet.SafetyNet.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SafetyNet.SafetyNet.model.MedicalRecord;
import com.SafetyNet.SafetyNet.repository.contracts.IMedicalRecordRepository;
import com.SafetyNet.SafetyNet.service.contracts.IMedicalRecordService;


@Service
public class MedicalRecordService implements IMedicalRecordService {

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordService.class);

    @Autowired
    private IMedicalRecordRepository iMedicalRecordRepository;

    @Override
    public List<MedicalRecord> getAllMedicalRecords() {
        return iMedicalRecordRepository.findAll();
    }

    @Override
    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
        for(MedicalRecord existingMedicalRecord : iMedicalRecordRepository.findAll()) {
            if(medicalRecord.getFirstName().equals(existingMedicalRecord.getFirstName()) && medicalRecord.getLastName().equals(existingMedicalRecord.getLastName())) {
                logger.error("Medical record already exists: {} {}.", medicalRecord.getFirstName(), medicalRecord.getLastName());
                return null;
            }
        }

        return iMedicalRecordRepository.save(medicalRecord);
    }

    @Override
    public MedicalRecord updateMedicalRecord(String firstName, String lastName, MedicalRecord medicalRecordUpdate) {
        MedicalRecord existingMedicalRecord = iMedicalRecordRepository.findByFirstNameAndLastName(firstName, lastName);

        if (existingMedicalRecord != null) {
            return iMedicalRecordRepository.update(existingMedicalRecord, medicalRecordUpdate);
        } else {
            logger.error("Medical record not found for update: {} {}.", firstName, lastName);
            return null;
        }
    }

    @Override
    public boolean deleteMedicalRecord(String firstName, String lastName) {
        MedicalRecord matchingMedicalRecord = iMedicalRecordRepository.findByFirstNameAndLastName(firstName, lastName);

        if (matchingMedicalRecord != null) {
            iMedicalRecordRepository.deleteByFirstNameAndLastName(firstName, lastName);
            return true;
        } else {
            logger.error("Medical record not found for deletion: {} {}.", firstName, lastName);
            return false;
        }
    }

    @Override
    public int calculateAge(LocalDate birthdate) {
        int age = Period.between(birthdate, LocalDate.now()).getYears();

        logger.debug("Age calculated successfully: {}.", age);
        return age;
    }
}