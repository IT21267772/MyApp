package com.digitalscanner.backend.repo;

import com.digitalscanner.backend.models.Fare;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FareRepository extends MongoRepository<Fare, String> {
}
