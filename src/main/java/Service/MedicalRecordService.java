package Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.MedicalRecordRepository;


@Service
public class MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;

    @Autowired
    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

}
