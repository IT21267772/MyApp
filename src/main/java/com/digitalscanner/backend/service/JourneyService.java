package com.digitalscanner.backend.service;

import com.digitalscanner.backend.exception.ResourceNotFoundException;
import com.digitalscanner.backend.models.Fare;
import com.digitalscanner.backend.models.Journey;
import com.digitalscanner.backend.repo.FareRepository;
import com.digitalscanner.backend.repo.JourneyRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JourneyService {
    @Autowired
    private JourneyRepository journeyRepository;

    @Autowired
    private FareRepository fareRepository;

    @Autowired
    private UserService userService;

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    public ResponseEntity<Journey> addJourney(Journey journey) {
        Journey savedJourney = journeyRepository.save(journey);
        return new ResponseEntity<>(savedJourney, HttpStatus.OK);
    }

    public ResponseEntity<List<Journey>> getJourneys() {
        List<Journey> journeys = journeyRepository.findAll();

        if (journeys.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(journeys, HttpStatus.OK);
        }
    }

    public ResponseEntity<Journey> getJourneyById(String id) {
        Optional<Journey> journey = journeyRepository.findById(id);

        if(journey.isPresent()) {
            return new ResponseEntity<>(journey.get(), HttpStatus.OK);
        }
        else {
            throw new ResourceNotFoundException("Journey not found with id: " + id);
        }
    }
    public ResponseEntity<List<Journey>> getJourneysByUser(String userId) {
            List<Journey> journeys = journeyRepository.findByUser(userId);
            if (journeys.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(journeys, HttpStatus.OK);
            }
    }

    public ResponseEntity<List<Journey>> getOngoingJourney(String userId) {
        List<Journey> journeys = journeyRepository.findByUserAndStatus(userId, "ongoing");
        if (journeys.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(journeys, HttpStatus.OK);
        }
    }

    public ResponseEntity<Journey> updateJourney(String id, Journey journey) {

        Optional<Journey> journeyData = journeyRepository.findById(id);

        if(journeyData.isPresent()) {
            Journey journeyToUpdate = journeyData.get();
            journeyToUpdate.setEnd(journey.getEnd());
            if (journey.getEndTime() != null) {
                journeyToUpdate.setEndTime(journey.getEndTime());
            } else {
                journeyToUpdate.setEndTime(LocalDateTime.now());
            }
            journeyToUpdate.setStatus("completed");
            journeyToUpdate.setDistance(calculateDistance(journeyToUpdate.getStart(), journeyToUpdate.getEnd()));
            if(journeyToUpdate.getBusType() != null) {
                journeyToUpdate.setCharge(Math.round(calculateFare(journeyToUpdate.getBusType(), journeyToUpdate.getDistance()) * 100.0) / 100.0);
            }
            userService.updateBalance(journeyToUpdate.getUser(), journeyToUpdate.getCharge());
            return new ResponseEntity<>(journeyRepository.save(journeyToUpdate), HttpStatus.OK);
        }
        else {
            throw new ResourceNotFoundException("Journey not found with id: " + id);
        }
    }

    public Boolean deleteJourney(String id) {

        Optional<Journey> journeyData = journeyRepository.findById(id);

        if(journeyData.isPresent()) {
            journeyRepository.deleteById(id);
            return true;
        }
        else {
            throw new ResourceNotFoundException("Journey not found with id: " + id);
        }
    }


    // Method to calculate the distance using Google Maps API
    private double calculateDistance(String origin, String destination) {

        String apiUrl = "https://maps.googleapis.com/maps/api/distancematrix/json";

        // Build the API URL with query parameters
        String fullUrl = apiUrl + "?origins=" + origin + "&destinations=" + destination + "&key=" + googleMapsApiKey;

        // Make an HTTP GET request to the API
        ResponseEntity<String> response = new RestTemplate().getForEntity(fullUrl, String.class);

        // Parse the response to extract the distance
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode rows = rootNode.get("rows");

            if (rows != null && rows.size() > 0) {
                JsonNode elements = rows.get(0).get("elements");

                if (elements != null && elements.size() > 0) {
                    JsonNode distance = elements.get(0).get("distance");
                    if (distance != null && distance.has("value")) {
                        int distanceValue = distance.get("value").asInt();

                        // Convert meters to kilometers
                        double distanceInKm = distanceValue / 1000.0;
                        return distanceInKm;
                    }
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // Return a default value if parsing fails
        return -1.0;
    }

    private double calculateFare(String busType, double distance) {

        Optional<Fare> fareData = fareRepository.findById(busType);

        if (fareData.isPresent()) {
            Fare fare = fareData.get();
            double priceFirst5Km = fare.getPriceFirst5Km();
            double price5KmTo20Km = fare.getPrice5KmTo20Km();
            double priceMoreThan20Km = fare.getPriceMoreThan20Km();

            if (distance <= 5.0) {
                return priceFirst5Km * distance;
            } else if (distance <= 20.0) {
                return priceFirst5Km * 5.0 + price5KmTo20Km * (distance - 5.0);
            } else {
                return priceFirst5Km * 5.0 + price5KmTo20Km * 15.0 + priceMoreThan20Km * (distance - 20.0);
            }
        } else {
            throw new ResourceNotFoundException("Fare not found for " + busType);
        }
    }
}
