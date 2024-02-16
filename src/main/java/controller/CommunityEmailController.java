package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.PersonService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CommunityEmailController {

    private final PersonService personService;

    @Autowired
    public CommunityEmailController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/communityEmail")
    public ResponseEntity<List<String>> getEmailsByCity(@RequestParam("city") String city) {
        List<String> emails = personService.getEmailsByCity(city);
        return ResponseEntity.ok(emails);
    }
}

