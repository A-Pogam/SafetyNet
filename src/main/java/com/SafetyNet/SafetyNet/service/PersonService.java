package com.SafetyNet.SafetyNet.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SafetyNet.SafetyNet.dto.ChildInfo;
import com.SafetyNet.SafetyNet.model.MedicalRecord;
import com.SafetyNet.SafetyNet.model.Person;
import com.SafetyNet.SafetyNet.repository.contracts.IMedicalRecordRepository;
import com.SafetyNet.SafetyNet.repository.contracts.IPersonRepository;
import com.SafetyNet.SafetyNet.service.contracts.IMedicalRecordService;
import com.SafetyNet.SafetyNet.service.contracts.IPersonService;

@Service
public class PersonService implements IPersonService {

    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);

    @Autowired
    private IMedicalRecordService iMedicalRecordService;

    @Autowired
    private IPersonRepository iPersonRepository;
    @Autowired
    private IMedicalRecordRepository iMedicalRecordRepository;

    @Override
    public List<Person> getAllPersons() {
        return iPersonRepository.findAll();
    }

    @Override
    public Person addPerson(Person person) {
        for(Person existingPerson : iPersonRepository.findAll()) {
            if(person.getFirstname().equals(existingPerson.getFirstname()) && person.getLastname().equals(existingPerson.getLastname())) {
                logger.error("Person already exists: {} {}.", person.getFirstname(), person.getLastname());
                return null;
            }
        }

        return iPersonRepository.save(person);
    }

    @Override
    public Person updatePerson(String firstName, String lastName, Person personUpdate) {
        Person existingPerson = iPersonRepository.findByFirstNameAndLastName(firstName, lastName);

        if (existingPerson != null) {
            return iPersonRepository.update(existingPerson, personUpdate);
        } else {
            logger.error("Person not found for update: {} {}.", firstName, lastName);
            return null;
        }
    }

    @Override
    public boolean deletePerson(String firstName, String lastName) {
        Person matchingPerson = iPersonRepository.findByFirstNameAndLastName(firstName, lastName);

        if (matchingPerson != null) {
            iPersonRepository.deleteByFirstNameAndLastName(firstName, lastName);
            return true;
        } else {
            logger.error("Person not found for deletion: {} {}.", firstName, lastName);
            return false;
        }
    }

    @Override
    public List<ChildInfo> getChildAlert(String address) {
        logger.debug("Received request to get child alert for address: {}.", address);

        // Créer une liste pour stocker les informations sur les enfants
        List<ChildInfo> childrenInfos = new ArrayList<>();
        // Récupérer la liste des résidents de l'adresse spécifiée
        List<Person> residents = iPersonRepository.findByAddress(address);

        // Parcourir la liste des résidents pour trouver les enfants
        for (Person resident : residents) {
            // Get medical record for the resident
            MedicalRecord medicalRecord = iMedicalRecordRepository.findByFirstNameAndLastName(resident.getFirstname(), resident.getLastname());
            // Calculate age based on birthdate using FireStationService
            int age = iMedicalRecordService.calculateAge(medicalRecord.getBirthdate());
            // Get other members of the household
            List<Person> householdMembers = iPersonRepository.findHouseholdMembersByPerson(resident);

            ChildInfo childInfo = new ChildInfo(resident.getFirstname(), resident.getLastname(), age, householdMembers);

            if (childInfo != null && childInfo.getAge() <= 18) {
                // Récupérer les autres membres du foyer (enfants exclus)
                List<Person> parents = residents.stream().filter(person -> !person.equals(resident))
                        .collect(Collectors.toList());
                // Ajouter les autres membres du foyer à l'objet ChildInfo
                childInfo.setHouseholdMembers(parents);

                // Ajouter l'enfant à la liste des enfants
                childrenInfos.add(childInfo);
            }
        }

        logger.info("Found {} children in the household for address: {}.", childrenInfos.size(), address);
        return childrenInfos;
    }

    @Override
    public List<String> getEmailsByCity(String city) {
        logger.debug("Received request to get emails for city: {}.", city);
        List<String> emailsByCity = new ArrayList<String>();

        for(String email : iPersonRepository.findEmailsByCity(city)) {
            if(!emailsByCity.contains(email)) {
                emailsByCity.add(email);
            }
        }

        logger.info("Emails found for city: {}.", city);
        return emailsByCity;
    }

    @Override
    public Map<String, Object> getPersonInfo(String firstName, String lastName) {
        logger.debug("Received request to get person info: {} {}.", firstName, lastName);

        Person person = iPersonRepository.findByFirstNameAndLastName(firstName, lastName);

        if (person == null) {
            logger.error("Person not found: {} {}.", firstName, lastName);
            return null;
        }

        // Créer une carte pour stocker les détails de cette personne
        Map<String, Object> personDetails = new HashMap<>();
        personDetails.put("name", person.getFirstname() + " " + person.getLastname());
        personDetails.put("address", person.getAddress());
        personDetails.put("email", person.getEmail());
        personDetails.put("phone", person.getPhone());

        // Récupérer le dossier médical de la personne
        MedicalRecord medicalRecord = iMedicalRecordRepository.findByFirstNameAndLastName(firstName, lastName);
        if (medicalRecord != null) {
            // Calculer l'âge à partir de la date de naissance
            int age = iMedicalRecordService.calculateAge(medicalRecord.getBirthdate());

            personDetails.put("age", age);
            personDetails.put("medications", medicalRecord.getMedications());
            personDetails.put("allergies", medicalRecord.getAllergies());
        }

        logger.info("Person info found: {} {}.", firstName, lastName);
        return personDetails;
    }
}