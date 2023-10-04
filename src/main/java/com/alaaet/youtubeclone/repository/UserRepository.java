package com.alaaet.youtubeclone.repository;

import com.alaaet.youtubeclone.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findBySub(String sub);
}
