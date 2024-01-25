package repository;

import model.FireStation;

public interface FireStationRepository {
    FireStation findByAddress(String address);
}
