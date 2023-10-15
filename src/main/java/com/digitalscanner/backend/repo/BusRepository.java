package com.digitalscanner.backend.repo;

import com.digitalscanner.backend.models.Bus;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BusRepository extends MongoRepository<Bus, String> {
}
