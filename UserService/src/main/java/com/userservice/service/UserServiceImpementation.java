package com.userservice.service;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.userservice.DTO.UserDTO;
import com.userservice.entity.User;
import com.userservice.exception.DuplicateEntityException;
import com.userservice.exception.IncorrectPasswordException;
import com.userservice.exception.UserNotFoundException;
import com.userservice.repository.UserRepository;

import lombok.AllArgsConstructor;

/**
 * Service implementation class for managing user-related operations.
 */
@Service
@AllArgsConstructor
public class UserServiceImpementation implements UserService {

	private UserRepository userRepository;

	/**
	 * Logs in a user with the given username and password.
	 * 
	 * @param userName the username of the user
	 * @param password the password of the user
	 * @return a message indicating the login status
	 * @throws UserNotFoundException      if the user is not found
	 * @throws IncorrectPasswordException if the password is incorrect
	 */
	@Override
	public String loginUser(String userName, String password) {
		User user = userRepository.findById(userName)
				.orElseThrow(() -> new UserNotFoundException(userName + " not Found"));
		if (!Objects.equals(user.getPassword(), password)) {
			throw new IncorrectPasswordException("Wrong Password");
		}
		return "Logged In";
	}

	/**
	 * Registers a new user.
	 * 
	 * @param user the user details to register
	 * @return the registered user
	 * @throws DuplicateEntityException if the user exists in database
	 */
	@Override
	public User registerUser(User user) {
		if (userRepository.findById(user.getUserName()).isPresent()) {
			throw new DuplicateEntityException("Entity with usename " + user.getUserName() + " already exists.");
		}
		return userRepository.save(user);
	}

	/**
	 * Deletes a user with the given username.
	 * 
	 * @param userName the username of the user to delete
	 * @return a message indicating the deletion status
	 * @throws UserNotFoundException if the user is not found
	 */
	@Override
	public String deleteUser(String userName) {
		User user = userRepository.findById(userName)
				.orElseThrow(() -> new UserNotFoundException(userName + " not Found"));
		userRepository.delete(user);
		return "User Deleted";
	}

	/**
	 * Updates the details of an existing user.
	 * 
	 * @param userName the username of the user to update
	 * @param user     the updated user details
	 * @return the updated user
	 * @throws UserNotFoundException if the user is not found
	 */
	@Override
	public User updateUser(String userName, User user) {
		User existingUser = userRepository.findById(userName)
				.orElseThrow(() -> new UserNotFoundException(userName + " not Found"));
		existingUser.setFirstName(user.getFirstName());
		existingUser.setLastName(user.getLastName());
		existingUser.setEmail(user.getEmail());
		existingUser.setPassword(user.getPassword());
		existingUser.setContactNo(user.getContactNo());
		existingUser.setBirthDate(user.getBirthDate());
		existingUser.setGender(user.getGender());
		existingUser.setRole(user.getRole());

		return userRepository.save(existingUser);
	}

	/**
	 * Retrieves user details by username.
	 * 
	 * @param userName the username of the user to retrieve
	 * @return the user details as a UserDTO
	 * @throws UserNotFoundException if the user is not found
	 */
	@Override
	public UserDTO getByUserName(String userName) {
		User user = userRepository.findById(userName)
				.orElseThrow(() -> new UserNotFoundException(userName + " not Found"));
		UserDTO userDTO = new UserDTO();
		userDTO.setUserName(user.getUserName());
		userDTO.setFirstName(user.getFirstName());
		userDTO.setLastName(user.getLastName());
		userDTO.setEmail(user.getEmail());
		userDTO.setRole(user.getRole().name());
		return userDTO;
	}
}
