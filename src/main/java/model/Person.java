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


    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @OneToOne(mappedBy = "person") //One only entry by person, avoid to write again same date for same person
    private MedicalRecord medicalRecord;
}
