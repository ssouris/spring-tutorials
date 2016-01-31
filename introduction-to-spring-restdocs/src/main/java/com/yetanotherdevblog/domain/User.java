package com.yetanotherdevblog.domain;

import java.util.UUID;

/**
 * Simple User domain object
 */
public class User {
    
    private String firstName;
    private String lastName;
    private UUID userId;
    private String username;

    public User() {
    }
    
    public User(UUID userId, String username, String firstName, String lastName) {
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
}
