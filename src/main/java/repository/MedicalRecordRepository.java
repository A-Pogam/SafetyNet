package repository;

import model.MedicalRecord;

import java.util.List;

public interface MedicalRecordRepository {
    MedicalRecord findByFirstNameAndLastName(String firstName, String lastName);

    MedicalRecord findByFirstNameAndLastNameAndBirthdateAndMedicationsAndAllergies(
            String firstName, String lastName, String birthdate, List<String> medications, List<String> allergies
    );
}
