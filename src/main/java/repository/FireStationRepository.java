package repository;

import model.FireStation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FireStationRepository extends JpaRepository<FireStation, Long > {
    FireStation findByAddress(String address);

}
