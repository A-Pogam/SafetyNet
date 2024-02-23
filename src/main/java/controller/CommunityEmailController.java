package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.PersonService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CommunityEmailController {
    private static final Logger logger = LoggerFactory.getLogger(CommunityEmailController.class);


    private final PersonService personService;

    @Autowired
    public CommunityEmailController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/communityEmail")
    public ResponseEntity<List<String>> getEmailsByCity(@RequestParam("city") String city) {
        logger.info("Received request to get emails for city: {}", city);
        List<String> emails = personService.getEmailsByCity(city);
        if (emails != null) {
            logger.info("Found {} emails for city: {}", emails.size(), city);
            return ResponseEntity.ok(emails);
        } else {
            logger.error("Failed to retrieve emails for city: {}", city);
            return ResponseEntity.notFound().build();
        }
    }
}

