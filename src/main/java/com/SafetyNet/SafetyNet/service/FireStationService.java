package com.SafetyNet.SafetyNet.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SafetyNet.SafetyNet.dto.CoveredPerson;
import com.SafetyNet.SafetyNet.dto.FireStationCoverage;
import com.SafetyNet.SafetyNet.model.FireStation;
import com.SafetyNet.SafetyNet.model.MedicalRecord;
import com.SafetyNet.SafetyNet.model.Person;
import com.SafetyNet.SafetyNet.repository.contracts.IFireStationRepository;
import com.SafetyNet.SafetyNet.repository.contracts.IMedicalRecordRepository;
import com.SafetyNet.SafetyNet.repository.contracts.IPersonRepository;
import com.SafetyNet.SafetyNet.service.contracts.IFireStationService;
import com.SafetyNet.SafetyNet.service.contracts.IMedicalRecordService;

@Service
public class FireStationService implements IFireStationService {

    private static final Logger logger = LogManager.getLogger(FireStationService.class);

    @Autowired
    private IMedicalRecordService iMedicalRecordService;

    @Autowired
    private IPersonRepository iPersonRepository;
    @Autowired
    private IMedicalRecordRepository iMedicalRecordRepository;
    @Autowired
    private IFireStationRepository iFireStationRepository;

    @Override
    public List<FireStation> getAllFireStations() {
        return iFireStationRepository.findAll();
    }

    @Override
    public FireStation addMapping(FireStation fireStation) {
        for (FireStation existingFireStation : iFireStationRepository.findAll()) {
            if ((fireStation.getStation() == existingFireStation.getStation()) && fireStation.getAddress().equals(existingFireStation.getAddress())) {
                logger.error("Firestation mapping already exists: {} - {}.", fireStation.getStation(), fireStation.getAddress());
                return null;
            }
        }

        return iFireStationRepository.save(fireStation);
    }

    @Override
    public FireStation updateFireStationNumber(String address, int stationNumber, int newStationNumber) {
        FireStation existingFireStation = iFireStationRepository.findByAddressAndNumber(address, stationNumber);

        if (existingFireStation != null) {
            return iFireStationRepository.updateFireStationNumber(existingFireStation, newStationNumber);
        } else {
            logger.error("Firestation mapping not found for update: {} - {}.", address, stationNumber);
            return null;
        }
    }

    @Override
    public boolean deleteMapping(String address, int stationNumber) {
        FireStation matchingFireStation = iFireStationRepository.findByAddressAndNumber(address, stationNumber);

        if (matchingFireStation != null) {
            iFireStationRepository.deleteByAddressAndNumber(address, stationNumber);
            return true;
        } else {
            logger.error("Firestation mapping not found for deletion: {} - {}.", address, stationNumber);
            return false;
        }
    }

    @Override
    public FireStationCoverage getCoverageByStationNumber(int stationNumber) {
        logger.debug("Fetching coverage for fire station number: {}.", stationNumber);
        List<CoveredPerson> coveredPeople = new ArrayList<>();
        int adultsCount = 0;
        int childrenCount = 0;

        for (FireStation fireStation : iFireStationRepository.findAll()) {
            if (fireStation.getStation() == stationNumber) {
                for (Person person : iPersonRepository.findByAddress(fireStation.getAddress())) {
                    MedicalRecord medicalRecord = iMedicalRecordRepository.findByFirstNameAndLastName(person.getFirstname(), person.getLastname());

                    if (medicalRecord != null) {
                        int age = iMedicalRecordService.calculateAge(medicalRecord.getBirthdate());
                        coveredPeople.add(new CoveredPerson(person, age));

                        if (age > 18) {
                            adultsCount++;
                        } else {
                            childrenCount++;
                        }
                    }
                }
            }
        }

        FireStationCoverage coverageByFireStationNumber = new FireStationCoverage();
        coverageByFireStationNumber.setCoveredPeople(coveredPeople);
        coverageByFireStationNumber.setAdultsCount(adultsCount);
        coverageByFireStationNumber.setChildrenCount(childrenCount);

        logger.info("Coverage fetched successfully for fire station number: {}.", stationNumber);
        return coverageByFireStationNumber;
    }

    @Override
    public Map<String, Object> getResidentsAndFireStationByAddress(String address) {
        logger.debug("Fetching residents and fire station for address: {}.", address);

        // Récupérer les numéro des casernes desservant l'adresse donnée
        List<Integer> fireStationNumbers = iFireStationRepository.findFireStationNumberByAddress(address);

        if (fireStationNumbers.isEmpty()) {
            logger.error("No fire station found for address: {}.", address);
            return null; // Retourne null si aucune caserne de pompiers n'est trouvée pour l'adresse donnée
        }

        // Récupérer les habitants vivant à l'adresse donnée
        List<Person> residents = iPersonRepository.findByAddress(address);

        // Construire la réponse avec les informations des résidents et le numéro de la caserne
        List<Map<String, Object>> residentsInfo = new ArrayList<>();
        for (Person resident : residents) {
            Map<String, Object> residentInfo = new HashMap<>();
            residentInfo.put("firstName", resident.getFirstname());
            residentInfo.put("lastName", resident.getLastname());

            // Récupérer l'âge à partir du MedicalRecord
            MedicalRecord medicalRecord = iMedicalRecordRepository.findByFirstNameAndLastName(resident.getFirstname(), resident.getLastname());
            if (medicalRecord != null) {
                residentInfo.put("age", iMedicalRecordService.calculateAge(medicalRecord.getBirthdate()));
                residentInfo.put("medications", medicalRecord.getMedications());
                residentInfo.put("allergies", medicalRecord.getAllergies());
            }

            residentInfo.put("phone", resident.getPhone());
            residentsInfo.add(residentInfo);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("fireStationNumbers", fireStationNumbers);
        response.put("residents", residentsInfo);

        logger.info("Residents and fire station fetched successfully for address: {}.", address);
        return response;
    }

    @Override
    public List<Map<String, Object>> getFloodStations(List<Integer> stationNumbers) {
        // Créer une liste pour stocker les détails des foyers desservis par chaque caserne
        List<Map<String, Object>> floodStationsDetails = new ArrayList<>();
        // Obtenir les casernes d'incendie correspondant aux numéros de caserne fournis
        List<String> floodStationsAddresses = iFireStationRepository.findFireStationAddressesByStationNumbers(stationNumbers);

        // Pour chaque caserne d'incendie
        for (String floodStationAddress : floodStationsAddresses) {
            logger.debug("Fetching residents and firestation for address: {}.", floodStationAddress);
            // Récupérer les personnes vivant à cette adresse
            List<Person> residents = iPersonRepository.findByAddress(floodStationAddress);

            // Créer une liste pour stocker les détails des résidents de cette adresse
            List<Map<String, Object>> residentsDetails = new ArrayList<>();

            // Pour chaque résident
            for (Person resident : residents) {
                // Créer une carte pour stocker les détails de ce résident
                Map<String, Object> residentDetail = new HashMap<>();
                residentDetail.put("name", resident.getFirstname() + " " + resident.getLastname());
                residentDetail.put("phone", resident.getPhone());

                // Récupérer le dossier médical de la personne
                MedicalRecord medicalRecord = iMedicalRecordRepository.findByFirstNameAndLastName(resident.getFirstname(), resident.getLastname());
                if (medicalRecord != null) {
                    logger.debug("Fetching residents and medical records: {} {}.", medicalRecord.getFirstName(), medicalRecord.getLastName());
                    // Calculer l'âge à partir de la date de naissance
                    int age = iMedicalRecordService.calculateAge(medicalRecord.getBirthdate());
                    residentDetail.put("age", age);

                    // Ajouter les médicaments et allergies
                    residentDetail.put("medications", medicalRecord.getMedications());
                    residentDetail.put("allergies", medicalRecord.getAllergies());
                }

                // Ajouter les détails de ce résident à la liste
                residentsDetails.add(residentDetail);
            }

            // Créer une carte pour stocker les détails de cette adresse avec les résidents
            Map<String, Object> addressDetails = new HashMap<>();

            addressDetails.put("address", floodStationAddress);
            addressDetails.put("residents", residentsDetails);

            floodStationsDetails.add(addressDetails);
        }

        logger.info("Residents and their medical records fetched successfully with firestations asked.");
        return floodStationsDetails;
    }

    @Override
    public List<String> getPhoneNumbersServedByFireStation(int stationNumber) {
        logger.debug("Received request to get phone numbers for firestation: {}.", stationNumber);
        List<Person> personsCoveredByFireStation = new ArrayList<>();

        List<String> fireStationAddresses = iFireStationRepository.findFireStationAddressesByStationNumber(stationNumber);
        logger.debug("Fetching residents and firestation: {}.", stationNumber);
        for (String fireStationAddress : fireStationAddresses) {
            personsCoveredByFireStation.addAll(iPersonRepository.findByAddress(fireStationAddress));
        }

        List<String> phoneNumbers = iPersonRepository.findPhonesFromPersonList(personsCoveredByFireStation);

        logger.info("Retrieved phone numbers for firestation: {}.", stationNumber);
        return phoneNumbers;
    }
}