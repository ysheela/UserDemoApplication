package com.example.userdemo.Controllers;

import com.example.userdemo.domain.User;
import com.example.userdemo.repository.UserRepository;
import com.example.userexception.ResourceNotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user/")
public class UserController {

    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/all")
    @Transactional(readOnly = true)
    public Page<User> getAll(Model model, Pageable pageable){
        Page<User> pages = userRepository.findAll(pageable);
        model.addAttribute("number", pages.getNumber());
        model.addAttribute("totalPages", pages.getTotalPages());
        model.addAttribute("totalElements",
                pages.getTotalElements());
        model.addAttribute("size", pages.getSize());
        model.addAttribute("users", pages.getContent());
        return  pages;
    }

    @PutMapping
    public void Insert(@Valid @RequestBody User user)
    {
        try {
            this.userRepository.insert(user);
        }
        catch (com.mongodb.DuplicateKeyException ex)
        {
            throw new DuplicateKeyException("Cannot add user with same first and last name");
        }
    }

    @PostMapping
    public void Update(@Valid @RequestBody User user)
    {
        try {
            this.userRepository.save(user);
        }
        catch (com.mongodb.DuplicateKeyException ex)
        {
            throw new DuplicateKeyException("Cannot update user with same first and last name");
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id){
        this.userRepository.delete(id);
    }

    @GetMapping("/{id}")
    public User GetUser(@PathVariable("id") String id) {
        User foundUser = this.userRepository.findOneById(id);

        if (foundUser == null)
        {
            throw new ResourceNotFoundException(id,"user not found");
        }

        return foundUser;
    }
}