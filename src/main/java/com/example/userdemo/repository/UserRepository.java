package com.example.userdemo.repository;

import com.example.userdemo.domain.User;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findOneById(String id);
    @Query("{ distinct : 'Users', key : 'email'}")
    List<String> findDistinctEmailAddresses();
}
