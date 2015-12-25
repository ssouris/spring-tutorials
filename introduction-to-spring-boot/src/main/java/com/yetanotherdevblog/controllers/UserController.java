package com.yetanotherdevblog.controllers;

import com.yetanotherdevblog.domain.User;
import com.yetanotherdevblog.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(method = RequestMethod.GET)
    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{userId}")
    public User getUser(@PathVariable Long userId) {
        return userRepository.findOne(userId);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{userId}")
    public User updateUser(@PathVariable Long userId, @RequestBody User updatedUser, HttpServletResponse response) {
        User user = userRepository.findOne(userId);
        if (user == null) {
            response.setStatus(404);
            return null;
        }

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setUsername(updatedUser.getUsername());
        return userRepository.save(user);
    }

    @RequestMapping(method = RequestMethod.POST)
    public User insertUser(@RequestBody User newUser) {
        return userRepository.save(newUser);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userRepository.delete(userId);
    }

}
