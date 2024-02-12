package controller;

import model.Person;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.FireStationService;
import service.PersonService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PhoneAlertController {

    private final FireStationService fireStationService;

    public PhoneAlertController(FireStationService fireStationService) {
        this.fireStationService = fireStationService;
    }

    @GetMapping("/phoneAlert")
    public ResponseEntity<List<String>> getPhoneAlert(@RequestParam("firestation") List<Integer> firestations) {
        List<String> phoneNumbers = fireStationService.getPhoneNumbersServedByFireStations(firestations);
        if (phoneNumbers.isEmpty()) {
            return ResponseEntity.notFound().build(); // Retourne 404 si aucun numéro n'est trouvé
        }
        return ResponseEntity.ok(phoneNumbers);
    }

}
