package com.yetanotherdevblog.controllers;

import com.yetanotherdevblog.domain.User;
import com.yetanotherdevblog.datastores.UserDatastore;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple CRUD RESTful controller for {@linkplain  User} object
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    
    private final UserDatastore userDatastore;

    @Autowired
    public UserController(UserDatastore userRepository) {
        Assert.notNull(userRepository, "User repository cannot be null");
        this.userDatastore = userRepository;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public List<User> getUsers(
            @RequestParam("page") int page, 
            @RequestParam("size") int size) {
        return userDatastore.findAll(page, size);
    }
    
    @RequestMapping(method = RequestMethod.GET, value="/{userId}")
    public User getUser(@PathVariable UUID userId) {
        return userDatastore.findOne(userId);
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public User insertUser(@RequestBody User user) {
        return userDatastore.save(user);
    }
    
    @RequestMapping(method = RequestMethod.PUT, value="/{userId}")
    public User updateUser(@PathVariable UUID userId, @RequestBody User user) {
        Assert.isTrue(userId.equals(user.getUserId()));
        return userDatastore.updateUser(user);
    }
    
    @RequestMapping(method = RequestMethod.DELETE, value="/{userId}")
    public User deleteUser(@PathVariable UUID userId) {
        return userDatastore.deleteUser(userId);
    }
    
}
