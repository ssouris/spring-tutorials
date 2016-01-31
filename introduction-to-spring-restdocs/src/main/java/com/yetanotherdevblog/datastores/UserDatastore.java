package com.yetanotherdevblog.datastores;

import com.yetanotherdevblog.domain.User;
import java.util.List;
import java.util.UUID;

/**
 * Data store abstraction for {@linkplain User} domain object
 */
public interface UserDatastore  {
    
    /**
     * Find all users. Supports paginated results.
     * 
     * @param page the page of results
     * @param size the size of restult set
     * @return a list of users
     */
    List<User> findAll(int page, int size);

    /**
     * Find a user by their unique identifier.
     * 
     * @param userId user identifier
     * @return the user if exists else null
     */
    User findOne(UUID userId);

    /**
     * Insert a new user into the datastore.
     * 
     * @param user the user to insert
     * @return the user
     */
    User save(User user);

    /**
     * Update a user from the datastore. If user does not exist null us retured.
     * 
     * @param user the user to update
     * @return the user else null
     */
    User updateUser(User user);

    /**
     * Delete a user from the dastore.
     * 
     * @param userId user's identifier
     * @return the deleted user
     */
    User deleteUser(UUID userId);
    
}
