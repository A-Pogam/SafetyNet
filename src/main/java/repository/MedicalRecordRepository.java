package repository;

import model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    MedicalRecord findByFirstNameAndLastNameAndBirthdateAndMedicationsAndAllergies(
            String firstName, String lastName, String birthdate, List<String> medications, List<String> allergies
    );
}

