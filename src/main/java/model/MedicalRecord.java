package model;

import jakarta.persistence.*; //using Entity, id, JoinColum, ElementCollection, list(?), gereratedValue, OnetoOne

import java.util.List;


@Entity
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String birthdate;

    @JoinColumn(name = "person_id")
    private Person person;

    @ElementCollection
    private List<String> medications; //List is able to record several medications at one same place

    @ElementCollection
    private List<String> allergies;

}
