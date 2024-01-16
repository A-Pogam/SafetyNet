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

    public void saveAll(List<MedicalRecord> medicalRecords) {
        medicalRecordRepository.saveAll(medicalRecords);
    }

    public List<MedicalRecord> getAllMedicalRecords() {;
    return medicalRecordRepository.findAll();
    }
}
