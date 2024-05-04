package com.SafetyNet.SafetyNet.repository.contracts;

import java.util.List;

import com.SafetyNet.SafetyNet.model.FireStation;

public interface IFireStationRepository {

    List<FireStation> findAll();
    List<FireStation> findByAddress(String address);
    List<Integer> findFireStationNumberByAddress(String address);
    List<String> findFireStationAddressesByStationNumber(int stationNumber);
    List<String> findFireStationAddressesByStationNumbers(List<Integer> stationNumbers);
    FireStation findByAddressAndNumber(String address, int stationNumber);

    FireStation save(FireStation fireStation);
    FireStation updateFireStationNumber(FireStation existingFireStations, int newStationNumber);

    void deleteByAddressAndNumber(String address, int stationNumber);
}