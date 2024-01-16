package controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import java.util.List;

import model.FireStation;
import service.FireStationService;

@Controller
@RequestMapping("/firestations")
public class FireStationController {

    private final FireStationService fireStationService;

    @Autowired
    public FireStationController(FireStationService fireStationService) {
        this.fireStationService = fireStationService;
    }

    @GetMapping("/list")
    public String listFireStations(Model model) {
        List<FireStation> fireStations = fireStationService.getAllFireStations();
        model.addAttribute("fireStations", fireStations);
        return "firestation/list";
    }
}
