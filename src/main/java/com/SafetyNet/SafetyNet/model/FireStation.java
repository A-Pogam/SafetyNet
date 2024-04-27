package com.SafetyNet.SafetyNet.model;

import jakarta.persistence.Entity;

@Entity
public class FireStation {
    private String address;
    private int station;

    public FireStation() {
        // Constructeur par défaut
    }

    // Constructeur avec paramètres
    public FireStation(String address, int station) {
        this.address = address;
        this.station = station;
    }

    // Méthodes d'accès pour l'adresse
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Méthodes d'accès pour le numéro de caserne
    public int getStation() {
        return station;
    }

    public void setStation(int station) {
        this.station = station;
    }
}
