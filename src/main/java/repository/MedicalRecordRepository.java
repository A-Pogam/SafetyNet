package repository;

import model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    MedicalRecord findByFirstNameAndLastName(String firstName, String lastName);
}
