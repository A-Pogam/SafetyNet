package service;

import model.FireStation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.FireStationRepository;

import java.util.List;


@Service
public class FireStationService {

    private final FireStationRepository fireStationRepository;

    @Autowired
    public FireStationService(FireStationRepository fireStationRepository) {
        this.fireStationRepository = fireStationRepository;
    }

    public FireStation addMapping(FireStation fireStation) {
        return fireStationRepository.save(fireStation);
    }

    public FireStation updateFireStationNumber(String address, int stationNumber) {
        FireStation existingMapping = fireStationRepository.findByAddress(address);
        if (existingMapping != null) {
            existingMapping.setStation(stationNumber);
            return fireStationRepository.save(existingMapping);
        } else {
            return null;
        }
    }

    public boolean deleteMapping(String address) {
        FireStation existingMapping = fireStationRepository.findByAddress(address);
        if (existingMapping != null) {
            fireStationRepository.delete(existingMapping);
            return true;
        } else {
            return false;
        }
    }
}
