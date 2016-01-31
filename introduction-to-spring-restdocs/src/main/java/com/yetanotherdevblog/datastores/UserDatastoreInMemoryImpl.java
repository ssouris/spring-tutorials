package com.yetanotherdevblog.datastores;

import com.yetanotherdevblog.domain.User;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 * In memory implementation of {@linkplain UserDatastore}.
 * Login can be applied to all the methods implemented to fetch data from
 * any datastore. For example there might be an sql datastore and a cache 
 * datastore, and we want to look in the cache first and then in the sql datastore
 * to save some trips to the database.
 * 
 * In the current implementation an ever growing ArrayList is used.
 * This was done only for demonstrating the functionaliry of Spring Rest Docs
 * and shold never be used as is in production.
 */
@Service
public class UserDatastoreInMemoryImpl implements UserDatastore  {
    
    private final static List<User> USERS = new ArrayList<>();
    
    @Override
    public List<User> findAll(int page, int size) {
        return USERS;
    }

    @Override
    public User findOne(UUID userId) {
        return USERS.stream()
                    .filter(e -> e.getUserId().equals(userId))
                    .findFirst()
                    .orElse(null);
    }

    @Override
    public User save(User user) {
        if (user.getUserId() == null) {
            user.setUserId(UUID.randomUUID());
        }
        USERS.add(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        User userFromDatastore = findOne(user.getUserId());
        if (userFromDatastore == null) {
            return null;
        }
        
        userFromDatastore.setFirstName(user.getFirstName());
        userFromDatastore.setLastName(user.getLastName());
        userFromDatastore.setUserId(user.getUserId());
        userFromDatastore.setUsername(user.getUsername());
        return userFromDatastore;
    }

    @Override
    public User deleteUser(UUID userId) {
        User userFromDatastore = findOne(userId);
        if (userFromDatastore == null) {
            return null;
        }
        
        USERS.remove(userFromDatastore);
        return userFromDatastore;
    }
    
}
