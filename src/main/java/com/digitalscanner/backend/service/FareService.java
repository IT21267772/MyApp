package com.digitalscanner.backend.service;

import com.digitalscanner.backend.exception.ResourceNotFoundException;
import com.digitalscanner.backend.models.Fare;
import com.digitalscanner.backend.repo.FareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FareService {

    @Autowired
    private FareRepository fareRepository;

    public ResponseEntity<Fare> addFare(Fare fare) {
        Fare savedFare = fareRepository.save(fare);
        return new ResponseEntity<>(savedFare, HttpStatus.OK);
    }

    public ResponseEntity<List<Fare>> getFares() {
        List<Fare> fares = fareRepository.findAll();

        if(fares.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(fares, HttpStatus.OK);
        }
    }

    public ResponseEntity<Fare> getFareByBusType(String busType) {
        Optional<Fare> fare = fareRepository.findById(busType);

        if(fare.isPresent()) {
            return new ResponseEntity<>(fare.get(), HttpStatus.OK);
        }
        else {
            throw new ResourceNotFoundException("Fare not found for " + busType);
        }
    }

    public ResponseEntity<Fare> updateFare(String busType, Fare fare) {
        Optional<Fare> fareData = fareRepository.findById(busType);

        if(fareData.isPresent()) {
            Fare fareToUpdate = fareData.get();
            if(fare.getPriceFirst5Km() != null) {
                fareToUpdate.setPriceFirst5Km(fare.getPriceFirst5Km());
            }
            if(fare.getPriceFirst5Km() != null) {
                fareToUpdate.setPrice5KmTo20Km(fare.getPrice5KmTo20Km());
            }
            if(fare.getPriceFirst5Km() != null) {
                fareToUpdate.setPriceMoreThan20Km(fare.getPriceMoreThan20Km());
            }
            fareRepository.save(fareToUpdate);
            return new ResponseEntity<>(fareToUpdate, HttpStatus.OK);
        }
        else {
            throw new ResourceNotFoundException("Fare not found for " + busType);
        }
    }

    public Boolean deleteFare(String busType) {
        Optional<Fare> fare = fareRepository.findById(busType);

        if(fare.isPresent()) {
            fareRepository.deleteById(busType);
            return true;
        }
        else {
            throw new ResourceNotFoundException("Fare not found for " + busType);
        }
    }
}
