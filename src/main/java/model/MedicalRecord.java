package model;

import jakarta.persistence.*; //using Entity, id, JoinColum, ElementCollection, list(?), gereratedValue, OnetoOne

import java.util.List;


@Entity
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String birthdate;

    @ElementCollection
    private List<String> medications;

    @ElementCollection
    private List<String> allergies;

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public void setMedications(List<String> medications) {
        this.medications = medications;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public List<String> getMedications() {
        return medications;
    }

    public List<String> getAllergies() {
        return allergies;
    }

}
