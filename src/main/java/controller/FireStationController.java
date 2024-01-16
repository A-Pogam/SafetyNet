package controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import java.util.List;

import model.FireStation;
import service.FireStationService;

@Controller
@RequestMapping("/firestation")
public class FireStationController {

    private final FireStationService fireStationService;

    @Autowired
    public FireStationController(FireStationService fireStationService) {
        this.fireStationService = fireStationService;
    }

    @PostMapping
    public ResponseEntity<FireStation> addMapping(@RequestBody FireStation fireStation) {
        FireStation addedMapping = fireStationService.addMapping(fireStation);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedMapping);
    }

    @PutMapping("/{address}")
    public ResponseEntity<FireStation> updateFireStationNumber(@PathVariable String address, @RequestParam int stationNumber) {
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
}
