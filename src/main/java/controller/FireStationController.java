package controller;

import dto.FireStationCoverage;
import model.FireStation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.FireStationService;

import java.util.List;

@RestController //no need of ResponseBody with RestController
@RequestMapping("/firestation")
public class FireStationController {

    private final FireStationService fireStationService;

    public FireStationController(FireStationService fireStationService) {
        this.fireStationService = fireStationService;
    }


    @GetMapping
    public ResponseEntity<List<FireStation>> getAllFireStations() {
        List<FireStation> fireStations = fireStationService.getAllFireStations();
        return ResponseEntity.ok(fireStations);
    }


    @PostMapping
    public ResponseEntity<FireStation> addMapping(@RequestBody FireStation fireStation) {
        FireStation addedMapping = fireStationService.addMapping(fireStation);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedMapping);
    }


    @PutMapping("/{address}")
    public ResponseEntity<FireStation> updateFireStationNumber(
            @PathVariable String address,
            @RequestParam int stationNumber
    ) {
        FireStation updatedMapping = fireStationService.updateFireStationNumber(address, stationNumber);
        if (updatedMapping != null) {
            return ResponseEntity.ok(updatedMapping);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{address}")
    public ResponseEntity<Void> deleteMapping(@PathVariable String address) {
        boolean deleted = fireStationService.deleteMapping(address);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/delete/{address}")
    public String showDeleteForm(@PathVariable String address, Model model) {
        FireStation fireStation = new FireStation();
        fireStation.setAddress(address);
        model.addAttribute("fireStation", fireStation);
        return "firestation/deleteMapping";
    }

    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<FireStationCoverage> getFireStationCoverage(@RequestParam int stationNumber) {
        FireStationCoverage coverage = fireStationService.getCoverageByStationNumber(stationNumber);
        if (coverage != null) {
            return ResponseEntity.ok(coverage);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}