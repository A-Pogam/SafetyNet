package model;

import jakarta.persistence.*; //using Entity, id, GereratedValue, OnetoOne

@Entity //entity JPA(Java Persistence API)/representing a line in DB//Mapped in a table in DB.
public class Person {

    @Id //Primary key to identify a unique record in DB
    @GeneratedValue(strategy = GenerationType.IDENTITY) //used to specify how the value of an entity's primary key should be generated when the entity is inserted into the database
    private Long id; //unique ID of a person

    private String firstname;
    private String lastname;
    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;

    @OneToOne(mappedBy = "person") //One only entry by person, avoid to write again same date for same person
    private MedicalRecord medicalRecord;
}
