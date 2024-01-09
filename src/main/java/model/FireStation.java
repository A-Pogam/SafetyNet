package model;

import jakarta.persistence.*;

@Entity
public class FireStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String address;
    private String station;
}
