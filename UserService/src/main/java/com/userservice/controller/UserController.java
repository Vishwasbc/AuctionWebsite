package com.userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.userservice.DTO.UserDTO;
import com.userservice.entity.User;
import com.userservice.service.UserService;

import lombok.AllArgsConstructor;

/**
 * Controller class for managing user-related operations.
 */
@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {
	private UserService userService;

	/**
	 * Handles user login requests.
	 * 
	 * @param userName the username of the user
	 * @param password the password of the user
	 * @return a ResponseEntity containing the login status
	 */
	@GetMapping("/login")
	public ResponseEntity<String> login(@RequestParam String userName, @RequestParam String password) {
		return ResponseEntity.ok(userService.loginUser(userName, password));
	}
	

	/**
	 * Handles user registration requests.
	 * 
	 * @param user the user details to register
	 * @return a ResponseEntity containing the registered user and HTTP status
	 *         CREATED
	 */
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody User user) {
		return new ResponseEntity<>(userService.registerUser(user), HttpStatus.CREATED);
	}

	/**
	 * Handles user deletion requests.
	 * 
	 * @param userName the username of the user to delete
	 * @return a ResponseEntity containing the deletion status
	 */
	@DeleteMapping("/delete")
	public ResponseEntity<String> delete(@RequestParam String userName) {
		return ResponseEntity.ok(userService.deleteUser(userName));
	}

	/**
	 * Handles user update requests.
	 * 
	 * @param userName the username of the user to update
	 * @param user     the updated user details
	 * @return a ResponseEntity containing the updated user
	 */
	@PutMapping("/update/{userName}")
	public ResponseEntity<User> update(@PathVariable String userName, @RequestBody User user) {
		return ResponseEntity.ok(userService.updateUser(userName, user));
	}

	/**
	 * Retrieves user details by username.
	 * 
	 * @param userName the username of the user to retrieve
	 * @return the user details as a UserDTO
	 */
	@GetMapping("/{userName}")
	public UserDTO getByUserName(@PathVariable String userName) {
		return userService.getByUserName(userName);
	}
}
