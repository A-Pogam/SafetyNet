package service;

import dto.CoveredPerson;
import dto.FireStationCoverage;
import model.FireStation;

import model.MedicalRecord;
import model.Person;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;

@Service
public class FireStationService {

    private final List<FireStation> fireStations;
    private final PersonService personService;



    public FireStationService(List<FireStation> fireStations, PersonService personService) {
        this.fireStations = fireStations;
        this.personService = personService;
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
        FireStationCoverage coverage = new FireStationCoverage();
        List<CoveredPerson> coveredPeople = new ArrayList<>();

        for (FireStation fireStation : fireStations) {
            if (fireStation.getStation() == stationNumber) {
                List<Person> persons = personService.getPersonsByAddress(fireStation.getAddress());
                for (Person person : persons) {
                    MedicalRecord medicalRecord = personService.getMedicalRecordByName(person.getFirstname(), person.getLastname());
                    if (medicalRecord != null) {
                        int age = calculateAge(medicalRecord.getBirthdate());
                        coveredPeople.add(new CoveredPerson(person, age));
                    }
                }
            }
        }

        coverage.setCoveredPeople(coveredPeople);
        coverage.setAdultsCount((int) countAdults(coveredPeople));
        coverage.setChildrenCount((int) countChildren(coveredPeople));

        return coverage;
    }




    private int calculateAge(String birthdate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
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
    }
