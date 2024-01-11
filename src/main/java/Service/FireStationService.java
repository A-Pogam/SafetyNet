package Service;

import model.FireStation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.FireStationRepository;


@Service
public class FireStationService {
    private final FireStationRepository fireStationRepository;

    @Autowired
    public FireStationService(FireStationRepository fireStationRepository) {
        this.fireStationRepository = fireStationRepository;
    }

}