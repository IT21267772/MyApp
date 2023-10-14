package com.digitalscanner.backend.controllers;

import com.digitalscanner.backend.models.Fare;
import com.digitalscanner.backend.service.FareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fares")
public class FareController {

    @Autowired
    private FareService fareService;

    @PostMapping
    public ResponseEntity<Fare> addFare(@RequestBody Fare fare) {
        return fareService.addFare(fare);
    }

    @GetMapping
    public ResponseEntity<List<Fare>> getFares() {
        return fareService.getFares();
    }

    @GetMapping("/{busType}")
    public ResponseEntity<Fare> getFareByBusType(@PathVariable String busType) {
        return fareService.getFareByBusType(busType);
    }

    @PutMapping("/{busType}")
    public ResponseEntity<Fare> updateFare(@PathVariable String busType, @RequestBody Fare fare) {
        return fareService.updateFare(busType, fare);
    }

    @DeleteMapping("/{busType}")
    public Boolean deleteFare(@PathVariable String busType) {
        return fareService.deleteFare(busType);
    }
}
