package com.digitalscanner.backend.serviceTests;

import com.digitalscanner.backend.exception.ResourceNotFoundException;
import com.digitalscanner.backend.models.Journey;
import com.digitalscanner.backend.repo.JourneyRepository;
import com.digitalscanner.backend.service.JourneyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class JourneyServiceTest {

    @InjectMocks
    private JourneyService journeyService;

    @Mock
    private JourneyRepository journeyRepository;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddJourney_Positive() {
        Journey journey = new Journey("1", "Colombo", null, LocalDateTime.now(), null,
                "WAP-7878", "Luxury", "Colombo-Kurunegala", "05",
                "ongoing", 0.0, 0.0, "JohnDoe");

        when(journeyRepository.save(journey)).thenReturn(journey);

        ResponseEntity<Journey> response = journeyService.addJourney(journey);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(journey.getId(), response.getBody().getId());
    }

    @Test
    public void testGetJourneys_Positive() {
        Journey journey1 = new Journey("1", "Colombo", null, LocalDateTime.now(), null,
                "WAP-7878", "Luxury", "Colombo-Kurunegala", "05",
                "ongoing", 0.0, 0.0, "JohnDoe");
        Journey journey2 = new Journey("2", "Panadura", null, LocalDateTime.now(), null,
                "BAS-9090", "Normal", "Panadura-Kandy", "17",
                "ongoing", 0.0, 0.0, "JohnDoe");
        List<Journey> journeys = Arrays.asList(journey1, journey2);

        when(journeyRepository.findAll()).thenReturn(journeys);

        ResponseEntity<List<Journey>> response = journeyService.getJourneys();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetJourneys_NoContent() {
        when(journeyRepository.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Journey>> response = journeyService.getJourneys();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetJourneyById_Positive() {
        Journey journey = new Journey("1", "Colombo", null, LocalDateTime.now(), null,
                "WAP-7878", "Luxury", "Colombo-Kurunegala", "05",
                "ongoing", 0.0, 0.0, "JohnDoe");

        when(journeyRepository.findById("1")).thenReturn(Optional.of(journey));

        ResponseEntity<Journey> response = journeyService.getJourneyById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("1", response.getBody().getId());
    }

    @Test
    public void testGetJourneyById_NotFound() {
        when(journeyRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> journeyService.getJourneyById("1"));
    }

    @Test
    public void testUpdateJourney_NotFound() {
        Journey journey = new Journey("1", "Colombo", "Kadawatha", LocalDateTime.now(), null,
                "WAP-7878", "Luxury", "Colombo-Kurunegala", "05",
                "ongoing", 0.0, 0.0, "JohnDoe");

        when(journeyRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> journeyService.updateJourney("1", journey));
    }


    @Test
    public void testDeleteJourney_Positive() {
        String journeyId = "1";
        Journey journey = new Journey();
        journey.setId(journeyId);
        when(journeyRepository.findById(journeyId)).thenReturn(Optional.of(journey));
        Boolean result = journeyService.deleteJourney(journeyId);
        assertTrue(result);
        Mockito.verify(journeyRepository, Mockito.times(1)).deleteById(journeyId);
    }

    @Test
    public void testDeleteJourney_NotFound() {
        when(journeyRepository.existsById("1")).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> journeyService.deleteJourney("1"));
    }

}
