package com.akhil.controller;

import com.akhil.model.User;
import com.akhil.repo.UserRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserRepo repo;

    @PostMapping("/api/user")
    public User createUser(@RequestBody @Valid User user){
        return repo.save(user);
    }
    @GetMapping("/api/users")
    public List<User> getUser(){

        return repo.findAll();
    }

    @GetMapping("/api/user/{id}")
    public User getUserById(@PathVariable Long id) throws Exception {
        Optional<User>optional=repo.findById(id);
        if(optional.isPresent()){
            return optional.get();
        }
        throw  new Exception("User not Found");
    }

    @PutMapping("/api/user/{id}")
    public  User updateUser(@PathVariable Long id,@RequestBody User userDetails) throws Exception {
        User existingUser = repo.findById(id)
                .orElseThrow(() -> new Exception("User not found with id: " + id));

        existingUser.setFullName(userDetails.getFullName());
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setPhone(userDetails.getPhone());
        existingUser.setRole(userDetails.getRole());
        existingUser.setUpdatedAt(LocalDateTime.now()); // Manually sets update timestamp

        return repo.save(existingUser);    }

    @DeleteMapping("/api/user/{id}")
    public String deleteUserById(@PathVariable Long id) throws Exception {
        User existingUser = repo.findById(id)
                .orElseThrow(() -> new Exception("User not found with id: " + id));

        repo.deleteById(id);
        return "User Deleted with id : "+id;

    }
}
