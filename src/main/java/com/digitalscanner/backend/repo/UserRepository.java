package com.digitalscanner.backend.repo;

import com.digitalscanner.backend.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
