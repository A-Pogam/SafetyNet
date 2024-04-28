package com.SafetyNet.SafetyNet.service;

import com.SafetyNet.SafetyNet.dto.CoveredPerson;
import com.SafetyNet.SafetyNet.dto.FireStationCoverage;
import com.SafetyNet.SafetyNet.model.FireStation;

import com.SafetyNet.SafetyNet.model.MedicalRecord;
import com.SafetyNet.SafetyNet.model.Person;
import com.SafetyNet.SafetyNet.repository.FireStationRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;

import java.util.stream.Collectors;


@Service
public class FireStationService {
    private static final Logger logger = LogManager.getLogger(FireStationService.class);


    private final List<FireStation> fireStations;
    private final PersonService personService;
    private final List<Person> persons;
    private final MedicalRecordService medicalRecordService;

    private final FireStationRepository fireStationRepository;

    public FireStationService(List<FireStation> fireStations, PersonService personService, List<Person> persons, MedicalRecordService medicalRecordService, FireStationRepository fireStationRepository)  {
        this.fireStations = fireStations;
        this.personService = personService;
        this.persons = persons != null ? persons : new ArrayList<>();
        this.medicalRecordService = medicalRecordService;
        this.fireStationRepository = fireStationRepository;
    }



    public List<FireStation> getAllFireStations() {
        return new ArrayList<>(fireStations);
    }

    public FireStation addMapping(FireStation fireStation) {
        logger.info("Adding fire station mapping: {}", fireStation);
        fireStations.add(fireStation);
        logger.info("Fire station mapping added successfully");
        return fireStation;
    }

    public FireStation updateFireStationNumber(String address, int stationNumber) {
        logger.info("Updating fire station number for address {}: {}", address, stationNumber);
        FireStation updatedMapping = fireStations.stream()
                .filter(existingMapping -> existingMapping.getAddress().equals(address))
                .findFirst()
                .orElse(null);
        if (updatedMapping != null) {
            updatedMapping.setStation(stationNumber);
            logger.info("Fire station number updated successfully");
        } else {
            logger.warn("Fire station mapping not found for address: {}", address);
        }
        return updatedMapping;
    }


    public boolean deleteMapping(String address) {
        logger.info("Deleting fire station mapping for address: {}", address);
        boolean removed = fireStations.removeIf(existingMapping -> existingMapping.getAddress().equals(address));
        if (removed) {
            logger.info("Fire station mapping deleted successfully for address: {}", address);
        } else {
            logger.warn("No fire station mapping found for address: {}", address);
        }
        return removed;
    }

    public FireStationCoverage getCoverageByStationNumber(int stationNumber) {
        logger.info("Fetching coverage for fire station number: {}", stationNumber);
        List<CoveredPerson> coveredPeople = new ArrayList<>();
        int adultsCount = 0;
        int childrenCount = 0;

        for (FireStation fireStation : fireStations) {
            if (fireStation.getStation() == stationNumber) {
                // Vérifiez si persons est null avant d'itérer
                if (persons != null) {
                    List<Person> persons = personService.getPersonsByAddress(fireStation.getAddress());
                    for (Person person : persons) {
                        MedicalRecord medicalRecord = medicalRecordService.getMedicalRecordByName(person.getFirstname(), person.getLastname());
                        if (medicalRecord != null && medicalRecord.getBirthdate() != null && !medicalRecord.getBirthdate().isEmpty()) {
                            int age = calculateAge(medicalRecord.getBirthdate());
                            coveredPeople.add(new CoveredPerson(person, age));
                            if (age > 18) {
                                adultsCount++;
                            } else {
                                childrenCount++;
                            }
                        }
                    }
                } else {
                    logger.warn("Persons list is null");
                }
            }
        }

        FireStationCoverage coverage = new FireStationCoverage();
        coverage.setCoveredPeople(coveredPeople);
        coverage.setAdultsCount(adultsCount);
        coverage.setChildrenCount(childrenCount);

        logger.info("Coverage fetched successfully for fire station number: {}", stationNumber);
        return coverage;
    }

    public Map<String, Object> getResidentsAndFireStation(String address) {
        logger.info("Fetching residents and fire station for address: {}", address);

        // Récupérer le numéro de la caserne desservant l'adresse donnée
        Integer fireStationNumber = getFireStationNumberByAddress(address);

        if (fireStationNumber == null) {
            logger.warn("No fire station found for address: {}", address);
            return null; // Retourne null si aucune caserne de pompiers n'est trouvée pour l'adresse donnée
        }

        // Récupérer les habitants vivant à l'adresse donnée
        List<Person> residents = personService.getPersonsByAddress(address);

        // Construire la réponse avec les informations des résidents et le numéro de la caserne
        Map<String, Object> response = new HashMap<>();
        response.put("fireStationNumber", fireStationNumber);
        response.put("residents", residents);

        logger.info("Residents and fire station fetched successfully for address: {}", address);
        return response;
    }


    public List<FireStation> getFloodStationsAsFireStations(List<Integer> stationNumbers) {
        return fireStations.stream()
                .filter(fs -> stationNumbers.contains(fs.getStation()))
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getFloodStations(List<Integer> stationNumbers) {
        // Obtenir les casernes d'incendie correspondant aux numéros de caserne fournis
        List<FireStation> floodStations = getFloodStationsAsFireStations(stationNumbers);

        // Créer une liste pour stocker les détails des foyers desservis par chaque caserne
        List<Map<String, Object>> floodStationsDetails = new ArrayList<>();

        // Pour chaque caserne d'incendie
        for (FireStation floodStation : floodStations) {
            // Récupérer l'adresse desservie par cette caserne
            String address = floodStation.getAddress();

            // Récupérer les personnes vivant à cette adresse
            List<Person> residents = personService.getPersonsByAddress(address);

            // Créer une liste pour stocker les détails des résidents de cette adresse
            List<Map<String, Object>> residentsDetails = new ArrayList<>();

            // Pour chaque résident
            for (Person resident : residents) {
                // Créer une carte pour stocker les détails de ce résident
                Map<String, Object> residentDetail = new HashMap<>();
                residentDetail.put("name", resident.getFirstname() + " " + resident.getLastname());
                residentDetail.put("phone", resident.getPhone());

                // Récupérer le dossier médical de la personne
                MedicalRecord medicalRecord = medicalRecordService.getMedicalRecordByName(resident.getFirstname(), resident.getLastname());
                if (medicalRecord != null) {
                    // Calculer l'âge à partir de la date de naissance
                    int age = calculateAge(medicalRecord.getBirthdate());
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
            addressDetails.put("address", address);
            addressDetails.put("residents", residentsDetails);

            floodStationsDetails.add(addressDetails);
        }

        logger.info("Flood stations fetched successfully for station numbers: {}", stationNumbers);
        return floodStationsDetails;
    }







    public int calculateAge(String birthdate) {
        logger.info("Calculating age from birthdate: {}", birthdate);
        if (birthdate == null || birthdate.isEmpty()) {
            logger.warn("Birthdate is null or empty");
            return -1; // Date de naissance non spécifiée
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date birthDate = sdf.parse(birthdate);

            LocalDate localBirthDate = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate currentDate = LocalDate.now();


            int age = Period.between(localBirthDate, currentDate).getYears();
            logger.info("Age calculated successfully: {}", age);
            return Period.between(localBirthDate, currentDate).getYears();

        } catch (ParseException e) {
            // Gérer les erreurs de conversion de date
            logger.error("Error occurred while parsing birthdate: {}", birthdate, e);
            return -1; // Valeur par défaut si l'âge ne peut pas être calculé
        }
    }


    private long countAdults(List<CoveredPerson> coveredPersons) {
        return coveredPersons.stream()
                .filter(person -> person.getAge() > 18)
                .count();
    }

    private long countChildren(List<CoveredPerson> coveredPersons) {
        return coveredPersons.stream()
                .filter(person -> person.getAge() <= 18)
                .count();
    }


    private String getFireStationAddress(int firestationNumber) {
        // Récupérer l'adresse de la caserne de pompiers spécifiée
        return fireStations.stream()
                .filter(fs -> fs.getStation() == firestationNumber)
                .findFirst()
                .map(FireStation::getAddress)
                .orElse(null); // Gérer le cas où aucune caserne de pompiers n'est trouvée pour le numéro spécifié
    }

    public List<String> getPhoneNumbersServedByFireStations(List<Integer> firestations) {
        List<String> phoneNumbers = new ArrayList<>();
        List<Person> allPersons = personService.getAllPersons();
        for (int firestation : firestations) {
            List<String> filteredPhoneNumbers = allPersons.stream()
                    .filter(person -> fireStations.stream()
                            .anyMatch(fs -> fs.getStation() == firestation && fs.getAddress().equalsIgnoreCase(person.getAddress())))
                    .map(Person::getPhone)
                    .collect(Collectors.toList());
            phoneNumbers.addAll(filteredPhoneNumbers);
        }
        return phoneNumbers;
    }


    public Integer getFireStationNumberByAddress(String address) {
        for (FireStation fireStation : fireStations) {
            if (fireStation.getAddress().equalsIgnoreCase(address)) {
                return fireStation.getStation();
            }
        }
        return null; // Retourne null si l'adresse n'est pas trouvée
    }





}
