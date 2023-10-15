package com.digitalscanner.backend.service;

import com.digitalscanner.backend.exception.ResourceNotFoundException;
import com.digitalscanner.backend.models.User;
import com.digitalscanner.backend.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public ResponseEntity<User> getUserById(String id) {
        Optional<User> user = userRepository.findById(id);

        if(user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        else {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
    }

    public void updateBalance(String id, Double charge) {
        Optional<User> user = userRepository.findById(id);

        if(user.isPresent()) {
            User userToUpdate = user.get();
            Query query = new Query(Criteria.where("id").is(id));
            Update update = new Update().set("balance", userToUpdate.getBalance() - (Math.round(charge * 100.0) / 100.0));
            mongoTemplate.updateFirst(query, update, User.class);
        }
        else {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
    }
}
