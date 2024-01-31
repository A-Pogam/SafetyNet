package service;

import model.FireStation;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FireStationService {

    private final List<FireStation> fireStations; // Liste pour stocker les FireStations


    public FireStationService(List<FireStation> fireStations) {
        this.fireStations = fireStations;
    }


    public List<FireStation> getAllFireStations() {
        return new ArrayList<>(fireStations);
    }

    public FireStation addMapping(FireStation fireStation) {
        fireStations.add(fireStation);
        return fireStation;
    }

    public FireStation updateFireStationNumber(String address, int stationNumber) {
        for (FireStation existingMapping : fireStations) {
            if (existingMapping.getAddress().equals(address)) {
                existingMapping.setStation(stationNumber);
                return existingMapping;
            }
        }
        return null;
    }
    public boolean deleteMapping(String address) {
        return fireStations.removeIf(existingMapping -> existingMapping.getAddress().equals(address));
    }
}
