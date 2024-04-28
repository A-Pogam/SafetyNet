package com.SafetyNet.SafetyNet.repository;

import com.SafetyNet.SafetyNet.model.FireStation;

import java.util.List;

public interface FireStationRepository {
    List<FireStation> findByAddress(String address);
    FireStation save(FireStation fireStation);

    List<FireStation> findAll();


    void deleteByAddress(String address);

    void deleteByStation(int stationNumber);

}
