package service;

import dto.CoveredPerson;
import dto.FireStationCoverage;
import model.FireStation;

import model.MedicalRecord;
import model.Person;
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


    public FireStationService(List<FireStation> fireStations, PersonService personService, List<Person> persons, MedicalRecordService medicalRecordService) {
        this.fireStations = fireStations;
        this.personService = personService;
        this.persons = persons;
        this.medicalRecordService = medicalRecordService;
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
        for (FireStation existingMapping : fireStations) {
            if (existingMapping.getAddress().equals(address)) {
                existingMapping.setStation(stationNumber);
                logger.info("Fire station number updated successfully");
                return existingMapping;
            }
        }
        logger.warn("Fire station mapping not found for address: {}", address);
        return null;
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
        List<CoveredPerson> coveredPeople = new ArrayList<>(); // Converti en variable locale
        int adultsCount = 0; // Converti en variable locale
        int childrenCount = 0; // Converti en variable locale

        for (FireStation fireStation : fireStations) {
            if (fireStation.getStation() == stationNumber) {
                List<Person> persons = personService.getPersonsByAddress(fireStation.getAddress());
                for (Person person : persons) {
                    MedicalRecord medicalRecord = medicalRecordService.getMedicalRecordByName(person.getFirstname(), person.getLastname());
                    if (medicalRecord != null) {
                        int age = calculateAge(medicalRecord.getBirthdate());
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

        FireStationCoverage coverage = new FireStationCoverage();
        coverage.setCoveredPeople(coveredPeople);
        coverage.setAdultsCount(adultsCount);
        coverage.setChildrenCount(childrenCount);

        logger.info("Coverage fetched successfully for fire station number: {}", stationNumber);
        return coverage;
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

    public List<FireStation> getFloodStations(List<Integer> stationNumbers) {
        return fireStations.stream()
                .filter(fs -> stationNumbers.contains(fs.getStation()))
                .collect(Collectors.toList());
    }



}
