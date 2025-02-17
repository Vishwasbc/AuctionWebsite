package com.userservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.userservice.DTO.UserDTO;
import com.userservice.entity.Role;
import com.userservice.entity.User;
import com.userservice.exception.DuplicateEntityException;
import com.userservice.exception.IncorrectPasswordException;
import com.userservice.exception.UserNotFoundException;
import com.userservice.repository.UserRepository;
import com.userservice.service.UserServiceImpementation;

class UserServiceApplicationTests {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserServiceImpementation userService;

	private User user;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		user = new User();
		user.setUserName("testUser");
		user.setPassword("testPassword");
		user.setFirstName("Test");
		user.setLastName("User");
		user.setEmail("test@example.com");
		user.setContactNo("1234567890");
		user.setBirthDate(new Date());
		user.setGender("Male");
		user.setRole(Role.ADMIN);
	}

	@Test
	void testLoginUser_Success() {
		when(userRepository.findById("testUser")).thenReturn(Optional.of(user));
		String result = userService.loginUser("testUser", "testPassword");
		assertEquals("Logged In", result);
	}

	@Test
	void testLoginUser_UserNotFound() {
		when(userRepository.findById("testUser")).thenReturn(Optional.empty());
		assertThrows(UserNotFoundException.class, () -> userService.loginUser("testUser", "testPassword"));
	}

	@Test
	void testLoginUser_IncorrectPassword() {
		when(userRepository.findById("testUser")).thenReturn(Optional.of(user));
		assertThrows(IncorrectPasswordException.class, () -> userService.loginUser("testUser", "wrongPassword"));
	}

	@Test
	void testRegisterUser_Success() {
		when(userRepository.findById("testUser")).thenReturn(Optional.empty());
		when(userRepository.save(user)).thenReturn(user);
		User result = userService.registerUser(user);
		assertEquals(user, result);
	}

	@Test
	void testRegisterUser_DuplicateEntity() {
		when(userRepository.findById("testUser")).thenReturn(Optional.of(user));
		assertThrows(DuplicateEntityException.class, () -> userService.registerUser(user));
	}

	@Test
	void testDeleteUser_Success() {
		when(userRepository.findById("testUser")).thenReturn(Optional.of(user));
		String result = userService.deleteUser("testUser");
		assertEquals("User Deleted", result);
		verify(userRepository, times(1)).delete(user);
	}

	@Test
	void testDeleteUser_UserNotFound() {
		when(userRepository.findById("testUser")).thenReturn(Optional.empty());
		assertThrows(UserNotFoundException.class, () -> userService.deleteUser("testUser"));
	}

	@Test
	void testUpdateUser_Success() {
		when(userRepository.findById("testUser")).thenReturn(Optional.of(user));
		User updatedUser = new User();
		updatedUser.setUserName("testUser");
		updatedUser.setFirstName("Updated");
		updatedUser.setLastName("User");
		updatedUser.setEmail("updated@example.com");
		updatedUser.setPassword("updatedPassword");
		updatedUser.setContactNo("0987654321");
		updatedUser.setBirthDate(new Date());
		updatedUser.setGender("Female");
		updatedUser.setRole(Role.ADMIN);

		when(userRepository.save(any(User.class))).thenReturn(updatedUser);
		User result = userService.updateUser("testUser", updatedUser);
		assertEquals(updatedUser, result);
	}

	@Test
	void testUpdateUser_UserNotFound() {
		when(userRepository.findById("testUser")).thenReturn(Optional.empty());
		assertThrows(UserNotFoundException.class, () -> userService.updateUser("testUser", user));
	}

	@Test
	void testGetByUserName_Success() {
		when(userRepository.findById("testUser")).thenReturn(Optional.of(user));
		UserDTO result = userService.getByUserName("testUser");
		assertEquals("testUser", result.getUserName());
		assertEquals("Test", result.getFirstName());
		assertEquals("User", result.getLastName());
		assertEquals("test@example.com", result.getEmail());
		assertEquals("ADMIN", result.getRole());
	}

	@Test
	void testGetByUserName_UserNotFound() {
		when(userRepository.findById("testUser")).thenReturn(Optional.empty());
		assertThrows(UserNotFoundException.class, () -> userService.getByUserName("testUser"));
	}
}