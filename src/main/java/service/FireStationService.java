package service;

import dto.CoveredPerson;
import dto.FireStationCoverage;
import model.FireStation;

import model.MedicalRecord;
import model.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class FireStationService {

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
        fireStations.add(fireStation);
        return fireStation;
    }

    public FireStation updateFireStationNumber(String address, int stationNumber) {
        for (FireStation existingMapping : fireStations) {
            if (existingMapping.getAddress().equals(address)) {
                existingMapping.setStation(stationNumber);
                return existingMapping;
            }
        }
        return null;
    }
    public boolean deleteMapping(String address) {
        return fireStations.removeIf(existingMapping -> existingMapping.getAddress().equals(address));
    }

    public FireStationCoverage getCoverageByStationNumber(int stationNumber) {
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

        return coverage;
    }


    public int calculateAge(String birthdate) {
        if (birthdate == null || birthdate.isEmpty()) {
            return -1; // Date de naissance non spécifiée
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date birthDate = sdf.parse(birthdate);

            LocalDate localBirthDate = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate currentDate = LocalDate.now();

            return Period.between(localBirthDate, currentDate).getYears();
        } catch (ParseException e) {
            // Gérer les erreurs de conversion de date
            e.printStackTrace();
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
    private static final Logger logger = LogManager.getLogger(FireStationService.class);

    public List<String> getPhoneNumbersServedByFireStations(List<Integer> firestations) {
        logger.debug("Entering getPhoneNumbersServedByFireStations method with firestations: {}", firestations);

        List<String> phoneNumbers = new ArrayList<>();
        for (int firestation : firestations) {
            String address = getFireStationAddress(firestation);
            if (address != null) {
                // Récupérer les numéros de téléphone des résidents desservis par cette caserne
                List<String> residentPhoneNumbers = persons.stream()
                        .filter(person -> person.getAddress().equals(address))
                        .map(Person::getPhone)
                        .collect(Collectors.toList());
                phoneNumbers.addAll(residentPhoneNumbers);
            }
        }

        logger.debug("Phone numbers served by fire stations: {}", phoneNumbers);
        return phoneNumbers;
    }


}
