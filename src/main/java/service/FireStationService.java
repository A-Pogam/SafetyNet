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

    public void saveAll(List<FireStation> fireStations) {
        fireStationRepository.saveAll(fireStations);
    }

    public List<FireStation> getAllFireStations() {
        return fireStationRepository.findAll();
    }
}