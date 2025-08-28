package com.example.messaging.auth.repository;

import com.example.messaging.auth.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends MongoRepository<User,String> {
    public User findByUsername(String username);
}
