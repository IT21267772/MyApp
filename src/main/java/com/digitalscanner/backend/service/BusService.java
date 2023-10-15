package com.digitalscanner.backend.service;

import com.digitalscanner.backend.exception.ResourceNotFoundException;
import com.digitalscanner.backend.models.Bus;
import com.digitalscanner.backend.repo.BusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BusService {

    @Autowired
    private BusRepository busRepository;

    public ResponseEntity<Bus> getBusById(String id) {
        Optional<Bus> bus = busRepository.findById(id);

        if(bus.isPresent()) {
            return new ResponseEntity<>(bus.get(), HttpStatus.OK);
        }
        else {
            throw new ResourceNotFoundException("Bus not found with id: " + id);
        }
    }

    public Integer getSeatCount(String id) {
        Optional<Bus> bus = busRepository.findById(id);

        if(bus.isPresent()) {
            return bus.get().getNumOfSeats();
        }
        else {
            throw new ResourceNotFoundException("Bus not found with id: " + id);
        }
    }
}
