package com.digitalscanner.backend.serviceTests;

import com.digitalscanner.backend.exception.ResourceNotFoundException;
import com.digitalscanner.backend.models.Fare;
import com.digitalscanner.backend.repo.FareRepository;
import com.digitalscanner.backend.service.FareService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class FareServiceTest {

    @InjectMocks
    private FareService fareService;

    @Mock
    private FareRepository fareRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddFare_Positive() {
        Fare fare = new Fare("Luxury", 10.0, 8.0, 6.0);

        when(fareRepository.save(fare)).thenReturn(fare);

        ResponseEntity<Fare> response = fareService.addFare(fare);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Luxury", response.getBody().getBusType());
    }

    @Test
    public void testGetFares_Positive() {
        Fare fare1 = new Fare("Luxury", 10.0, 8.0, 6.0);
        Fare fare2 = new Fare("Normal", 5.0, 4.0, 3.0);
        List<Fare> fares = Arrays.asList(fare1, fare2);

        when(fareRepository.findAll()).thenReturn(fares);

        ResponseEntity<List<Fare>> response = fareService.getFares();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetFares_NoContent() {
        when(fareRepository.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Fare>> response = fareService.getFares();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetFareByBusType_Positive() {
        Fare fare = new Fare("Luxury", 10.0, 8.0, 6.0);

        when(fareRepository.findById("Luxury")).thenReturn(Optional.of(fare));

        ResponseEntity<Fare> response = fareService.getFareByBusType("Luxury");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Luxury", response.getBody().getBusType());
    }

    @Test
    public void testGetFareByBusType_NotFound() {
        when(fareRepository.findById("Luxury")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> fareService.getFareByBusType("Luxury"));
    }

    @Test
    public void testUpdateFare_Positive() {
        Fare existingFare = new Fare("Luxury", 10.0, 8.0, 6.0);
        Fare updatedFare = new Fare("Luxury", 12.0, 9.0, 7.0);

        when(fareRepository.findById("Luxury")).thenReturn(Optional.of(existingFare));

        ResponseEntity<Fare> response = fareService.updateFare("Luxury", updatedFare);

        System.out.println(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(12.0, response.getBody().getPriceFirst5Km());
    }

    @Test
    public void testUpdateFare_NotFound() {
        Fare updatedFare = new Fare("Luxury", 12.0, 9.0, 7.0);

        when(fareRepository.findById("Luxury")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> fareService.updateFare("Luxury", updatedFare));
    }

    @Test
    public void testDeleteFare_Positive() {
        String busType = "Luxury";
        Fare fare = new Fare();
        fare.setBusType(busType);

        when(fareRepository.findById(busType)).thenReturn(Optional.of(fare));

        Boolean result = fareService.deleteFare(busType);

        assertTrue(result);
        Mockito.verify(fareRepository, Mockito.times(1)).deleteById(busType);
    }

    @Test
    public void testDeleteFare_NotFound() {
        when(fareRepository.findById("Luxury")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> fareService.deleteFare("Luxury"));
    }
}
