package com.userservice.service;

import com.userservice.DTO.UserDTO;
import com.userservice.entity.User;

/**
 * Service interface for managing user-related operations.
 */
public interface UserService {

    /**
     * Logs in a user with the given username and password.
     * 
     * @param userName the username of the user
     * @param password the password of the user
     * @return a message indicating the login status
     */
    String loginUser(String userName, String password);

    /**
     * Registers a new user.
     * 
     * @param user the user details to register
     * @return the registered user
     */
    User registerUser(User user);

    /**
     * Deletes a user with the given username.
     * 
     * @param userName the username of the user to delete
     * @return a message indicating the deletion status
     */
    String deleteUser(String userName);

    /**
     * Updates the details of an existing user.
     * 
     * @param userName the username of the user to update
     * @param user the updated user details
     * @return the updated user
     */
    User updateUser(String userName, User user);

    /**
     * Retrieves user details by username.
     * 
     * @param userName the username of the user to retrieve
     * @return the user details as a UserDTO
     */
    UserDTO getByUserName(String userName);
}
