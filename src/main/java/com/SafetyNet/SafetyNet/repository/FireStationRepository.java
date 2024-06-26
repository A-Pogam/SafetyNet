package com.SafetyNet.SafetyNet.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.SafetyNet.SafetyNet.model.FireStation;
import com.SafetyNet.SafetyNet.repository.contracts.IFireStationRepository;

@Repository
public class FireStationRepository implements IFireStationRepository {

    private static final Logger logger = LoggerFactory.getLogger(PersonRepository.class);

    private List<FireStation> fireStations = new ArrayList<>();

    @Override
    public List<FireStation> findAll() {
        logger.debug("Searching for all firestation.");
        return new ArrayList<>(fireStations);
    }

    @Override
    public List<FireStation> findByAddress(String address) {
        logger.debug("Searching for firestation at address: {}.", address);
        // Implémentation de la recherche par adresse
        return fireStations.stream()
                .filter(fireStation -> fireStation.getAddress().equalsIgnoreCase(address))
                .collect(Collectors.toList());
    }

    @Override
    public List<Integer> findFireStationNumberByAddress(String address) {
        logger.debug("Searching for firestation number at address: {}.", address);
        return fireStations.stream()
                .filter(fireStation -> fireStation.getAddress().equalsIgnoreCase(address))
                .map(FireStation::getStation)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findFireStationAddressesByStationNumber(int stationNumber) {
        logger.debug("Searching address for firestation s: {}.", stationNumber);
        return fireStations.stream()
                .filter(fireStation -> fireStation.getStation() == stationNumber)
                .map(FireStation::getAddress)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findFireStationAddressesByStationNumbers(List<Integer> stationNumbers) {
        logger.debug("Searching address for all given firestations.");
        return fireStations.stream()
                .filter(fireStation -> stationNumbers.contains(fireStation.getStation()))
                .map(FireStation::getAddress)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public FireStation findByAddressAndNumber(String address, int stationNumber) {
        logger.debug("Searching for firestation number {} at address: {}.", stationNumber, address);
        return fireStations.stream()
                .filter(fireStation -> fireStation.getAddress().equalsIgnoreCase(address) && fireStation.getStation() == stationNumber)
                .findFirst()
                .orElse(null);
    }

    @Override
    public FireStation save(FireStation fireStation) {
        logger.debug("Adding firestation mapping: {} - {}.", fireStation.getAddress(), fireStation.getStation());
        fireStations.add(fireStation);
        logger.info("Firestation mapping added successfully: {} - {}.", fireStation.getAddress(), fireStation.getStation());

        return fireStation;
    }

    @Override
    public FireStation updateFireStationNumber(FireStation existingFireStation, int newStationNumber) {
        logger.debug("Updating fire station {} at address: {}.", existingFireStation.getStation(), existingFireStation.getAddress());
        existingFireStation.setStation(newStationNumber);
        logger.info("Fire station number updated successfully: {} - {}.", existingFireStation.getAddress(), existingFireStation.getStation());

        return existingFireStation;
    }

    @Override
    public void deleteByAddressAndNumber(String address, int stationNumber) {
        logger.debug("Deleting fire station {} mapping at address: {}.", stationNumber, address);
        fireStations.removeIf(fireStation -> fireStation.getAddress().equalsIgnoreCase(address) && fireStation.getStation() == stationNumber);
        logger.info("Fire station deleted successfully: {} - {}", address, stationNumber);
    }
}