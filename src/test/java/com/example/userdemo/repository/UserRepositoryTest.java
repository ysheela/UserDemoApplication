package com.example.userdemo.repository;


import com.example.userdemo.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@DataMongoTest
public class UserRepositoryTest {

    @Autowired
    UserRepository repository;

    @Before
    public void setUp() {

        repository.deleteAll();

        User user1 = new User("abc","last","xyz@gmail.com");
        repository.save(user1);

        User user2 = new User("test2","lasttest2","test2@gmail.com");
        repository.save(user2);
    }

    @Test
    public void findAll() {
        List<User> result = repository.findAll();
        assertThat(result).hasSize(2);
        assertEquals("abc", result.get(0).getFirstName());
        assertEquals("last", result.get(0).getLastName());
        assertEquals("xyz@gmail.com", result.get(0).getEmail());

        assertEquals("test2", result.get(1).getFirstName());
        assertEquals("lasttest2", result.get(1).getLastName());
        assertEquals("test2@gmail.com", result.get(1).getEmail());
    }

    @Test(expected = DuplicateKeyException.class )
    public void insert_Fail_Duplicates() {
        User user2 = new User("test2","lasttest2","test2@gmail.com");
        repository.insert(user2);
    }

    @Test
    public void findUserById() {
        List<User> result = repository.findAll();

        User actual= repository.findOneById(result.get(0).getId());
        assertEquals("abc", actual.getFirstName());
        assertEquals("last", actual.getLastName());
        assertEquals("xyz@gmail.com", actual.getEmail());
    }

    @Test
    public void deleteUserById() {
        List<User> result = repository.findAll();
        User testData= repository.findOneById(result.get(0).getId());
        repository.delete(testData.getId());
        assertNull(repository.findOneById(testData.getId()));
    }

    @Test
    public void updateUser() {
        List<User> result = repository.findAll();
        User testData= repository.findOneById(result.get(0).getId());
        testData.setEmail("newValue@gmail.com");
        repository.save(testData);

        User updatedData = repository.findOneById(testData.getId());
        assertNotNull(updatedData);

        assertEquals("newValue@gmail.com", updatedData.getEmail());
    }

}

