package com.digitalscanner.backend.repo;

import com.digitalscanner.backend.models.Journey;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface JourneyRepository extends MongoRepository<Journey, String> {
    List<Journey> findByUser(String userId);

    List<Journey> findByUserAndStatus(String userId, String status);

    List<Journey> findByStatus(String status);

    Long countByRouteAndStatus(String route, String status);

    Long countByBusIdAndStatus(String route, String status);
}
