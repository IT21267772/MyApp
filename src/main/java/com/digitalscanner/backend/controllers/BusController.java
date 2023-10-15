package com.digitalscanner.backend.controllers;

import com.digitalscanner.backend.models.Bus;
import com.digitalscanner.backend.service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bus")
public class BusController {

    @Autowired
    private BusService busService;

    @GetMapping("/{id}")
    public ResponseEntity<Bus> getBusById(@PathVariable String id) {
        return busService.getBusById(id);
    }
}
