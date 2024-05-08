package com.SafetyNet.SafetyNet.service.contracts;

import java.util.List;
import java.util.Map;

import com.SafetyNet.SafetyNet.dto.FireStationCoverage;
import com.SafetyNet.SafetyNet.model.FireStation;

public interface IFireStationService {

    public List<FireStation> getAllFireStations();

    public FireStation addMapping(FireStation fireStation);
    public FireStation updateFireStationNumber(String address, int stationNumber, int newStationNumber);

    public boolean deleteMapping(String address, int stationNumber);

    public FireStationCoverage getCoverageByStationNumber(int stationNumber);
    public Map<String, Object> getResidentsAndFireStationByAddress(String address);
    public List<Map<String, Object>> getFloodStations(List<Integer> stationNumbers);
    public List<String> getPhoneNumbersServedByFireStation(int stationNumber);
}