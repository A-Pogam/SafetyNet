package com.SafetyNet.SafetyNet.repository.implementary;

import com.SafetyNet.SafetyNet.model.FireStation;
import com.SafetyNet.SafetyNet.repository.FireStationRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FireStationRepositoryImpl implements FireStationRepository {

    private final List<FireStation> fireStations = new ArrayList<>();

    @Override
    public List<FireStation> findAll() {
        return new ArrayList<>(fireStations);
    }

    @Override
    public List<FireStation> findByAddress(String address) {
        // Implémentation de la recherche par adresse
        return fireStations.stream()
                .filter(fireStation -> fireStation.getAddress().equalsIgnoreCase(address))
                .collect(Collectors.toList());
    }

    @Override
    public FireStation save(FireStation fireStation) {
        // Vérifier si la fireStation existe déjà dans la liste des fireStations
        boolean exists = fireStations.stream()
                .anyMatch(fs -> fs.getAddress().equals(fireStation.getAddress()) && fs.getStation() == fireStation.getStation());

        // Si la fireStation n'existe pas, l'ajouter à la liste des fireStations
        if (!exists) {
            fireStations.add(fireStation);
        } else {
            // Si la fireStation existe déjà, la mettre à jour
            fireStations.stream()
                    .filter(fs -> fs.getAddress().equals(fireStation.getAddress()) && fs.getStation() == fireStation.getStation())
                    .findFirst()
                    .ifPresent(existingFireStation -> {
                        existingFireStation.setAddress(fireStation.getAddress());
                        existingFireStation.setStation(fireStation.getStation());
                        // Mettre à jour d'autres propriétés de la fireStation si nécessaire
                    });
        }

        return fireStation;
    }

    @Override
    public void deleteByAddress(String address) {
        // Implémentation de la suppression par adresse
        Iterator<FireStation> iterator = fireStations.iterator();
        while (iterator.hasNext()) {
            FireStation fireStation = iterator.next();
            if (fireStation.getAddress().equals(address)) {
                iterator.remove();
            }
        }
    }

    @Override
    public void deleteByStation(int stationNumber) {
        // Implémentation de la suppression par numéro de station
        Iterator<FireStation> iterator = fireStations.iterator();
        while (iterator.hasNext()) {
            FireStation fireStation = iterator.next();
            if (fireStation.getStation() == stationNumber) {
                iterator.remove();
            }
        }
    }
}
