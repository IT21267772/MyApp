package com.digitalscanner.backend.serviceTests;

import com.digitalscanner.backend.exception.ResourceNotFoundException;
import com.digitalscanner.backend.models.Bus;
import com.digitalscanner.backend.repo.BusRepository;
import com.digitalscanner.backend.service.BusService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class BusServiceTest {

    @InjectMocks
    private BusService busService;

    @Mock
    private BusRepository busRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetBusById_Positive() {
        String busId = "1";
        Bus bus = new Bus(busId, "Luxury", 1234, 50, "Sugath");

        when(busRepository.findById(busId)).thenReturn(Optional.of(bus));

        ResponseEntity<Bus> response = busService.getBusById(busId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(busId, response.getBody().getId());
    }

    @Test
    public void testGetBusById_NotFound() {
        String busId = "1";

        when(busRepository.findById(busId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> busService.getBusById(busId));
    }

    @Test
    public void testGetSeatCount_Positive() {
        String busId = "1";
        Bus bus = new Bus(busId, "Luxury", 12345, 50, "Driver123");

        when(busRepository.findById(busId)).thenReturn(Optional.of(bus));

        Integer seatCount = busService.getSeatCount(busId);

        assertEquals(bus.getNumOfSeats(), seatCount);
    }

    @Test
    public void testGetSeatCount_NotFound() {
        String busId = "1";

        when(busRepository.findById(busId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> busService.getSeatCount(busId));
    }
}
