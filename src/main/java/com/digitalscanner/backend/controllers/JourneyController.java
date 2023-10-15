package com.digitalscanner.backend.controllers;

import com.digitalscanner.backend.models.Journey;
import com.digitalscanner.backend.service.JourneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/journeys")
public class JourneyController {

    @Autowired
    private JourneyService journeyService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @GetMapping
    public ResponseEntity<List<Journey>> getJourneys() {
        return journeyService.getJourneys();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Journey> getJourneyById(@PathVariable String id) {
       return journeyService.getJourneyById(id);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Journey>> getJourneysByUser(@PathVariable String userId) {
        return journeyService.getJourneysByUser(userId);
    }

    @GetMapping("/users/ongoing/{userId}")
    public ResponseEntity<List<Journey>> getOngoingJourney(@PathVariable String userId) {
        return journeyService.getOngoingJourney(userId);
    }

    @GetMapping("/ongoing/all")
    public ResponseEntity<List<Journey>> getAllOngoingJourneys() {
        return journeyService.getAllOngoingJourneys();
    }

    @PostMapping
    public ResponseEntity<Journey> addJourney(@RequestBody Journey journey) {
        return journeyService.addJourney(journey);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Journey> updateJourney(@PathVariable String id, @RequestBody Journey journey) {
        return journeyService.updateJourney(id, journey);
    }

    @DeleteMapping("/{id}")
    public Boolean deleteJourney(@PathVariable String id) {
        return journeyService.deleteJourney(id);
    }
}
