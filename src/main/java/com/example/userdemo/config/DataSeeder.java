package com.example.userdemo.config;


import com.example.userdemo.domain.User;
import com.example.userdemo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {
    private UserRepository userRepository;

    public DataSeeder(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }
    @Override
    public void run(String... strings) throws Exception {

        List<User> allUsers = new ArrayList<User>();
        allUsers.add(new User("Sheela", "Yedlapalli", "sheela.Yedlapalli@gmail.com"));
        allUsers.add(new User("Rishi", "Eti", "Rishi.Eti1@gmail.com"));
        allUsers.add(new User("George", "Ochoa", "George.Ochoa@gmail.com"));
        allUsers.add(new User("Jun", "Fu", "Jun.Fu@gmail.com"));
        allUsers.add(new User("Raj", "Khan", "Raj.Khan@gmail.com"));
        allUsers.add(new User("Ashish", "Arora", "Ashish.Arora@gmail.com"));
        allUsers.add(new User("Madhav", "Tripathi", "Madhav.Tripathi@gmail.com"));
        allUsers.add(new User("Kathy", "Thomas", "Kathy.Thomas@gmail.com"));
        allUsers.add(new User("Sailaja", "Erram", "Sailaja.Erram@gmail.com"));
        allUsers.add(new User("Varun", "Thota", "Varun.Thota@gmail.com"));
        allUsers.add(new User("Kavya", "Erram", "Kavya.Erram@gmail.com"));
        allUsers.add(new User("Rakesh", "Edara", "Rakesh.Edara@gmail.com"));
        allUsers.add(new User("Yash", "Chopra", "Yash.Chopra@gmail.com"));
        allUsers.add(new User("Bill", "Gates", "Bill.Gates@gmail.com"));
        allUsers.add(new User("Hillary", "Rodham", "Hillary.Rodham@gmail.com"));
        allUsers.add(new User("Savitri", "Reddy", "Savitri.Reddy@gmail.com"));
        allUsers.add(new User("Sangeetha", "Rao", "Sangeetha.Rao@gmail.com"));
        allUsers.add(new User("Pavani", "Kumar", "Pavani.Kumar@gmail.com"));
        allUsers.add(new User("Aparna", "Sharma", "Aparna.Sharma@gmail.com"));
        allUsers.add(new User("Swathi", "Reddy", "Swathi.Reddy@gmail.com"));
        allUsers.add(new User("Maithili", "Reddy", "Maithili.Reddy@gmail.com"));

        //Drop All Hotels
        userRepository.deleteAll();
        userRepository.save(allUsers);
    }
}
