package com.yetanotherdevblog.controllers;

import com.yetanotherdevblog.datastores.UserDatastore;
import com.yetanotherdevblog.domain.User;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Simple CRUD RESTful controller for {@linkplain  User} object
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    
    private final UserDatastore userDatastore;

    public UserController(UserDatastore userRepository) {
        this.userDatastore = userRepository;
    }
    
    @GetMapping
    public List<User> getUsers(
            @RequestParam("page") int page, 
            @RequestParam("size") int size) {
        return userDatastore.findAll(page, size);
    }
    
    @GetMapping("/{userId}")
    public User getUser(@PathVariable UUID userId) {
        return userDatastore.findOne(userId);
    }
    
    @PostMapping
    public User insertUser(@RequestBody User user) {
        return userDatastore.save(user);
    }
    
    @PutMapping("/{userId}")
    public User updateUser(@PathVariable UUID userId, @RequestBody User user) {
        Assert.isTrue(userId.equals(user.getUserId()));
        return userDatastore.updateUser(user);
    }
    
    @DeleteMapping("/{userId}")
    public User deleteUser(@PathVariable UUID userId) {
        return userDatastore.deleteUser(userId);
    }
    
}
