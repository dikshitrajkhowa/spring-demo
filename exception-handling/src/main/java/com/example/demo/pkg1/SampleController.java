package com.example.demo.pkg1;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//SampleController.java
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.UserNotFoundException;

@RestController
@RequestMapping("/api")
public class SampleController {

	private final UserService userService;

	@Autowired
	public SampleController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/sample")
	public GenericResponse<String> getSampleData() {
		String sampleData = "Hello, world!";

		GenericResponse<String> response = new GenericResponse<>("Data retrieved successfully", true, sampleData);

		return response;
	}
	
	@GetMapping("/{userId}")
    public ResponseEntity<GenericResponse<User>> getUserById(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            return ResponseEntity.ok(new GenericResponse<>("User fetched successfully", true, user));
        } catch (UserNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new GenericResponse<>("User not found", false, null));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericResponse<>("An error occurred", false, null));
        }
    }

	@GetMapping("/users")
	public ResponseEntity<GenericResponse<List<User>>> getUsers() {
		try {
			// Simulating fetching users from a database
			List<User> users = getUsersFromDatabase();

			// Create a generic success response with the user data
			GenericResponse<List<User>> response = new GenericResponse<>("Users fetched successfully", true, users);
			return ResponseEntity.ok(response);
		} catch (Exception ex) {
			// If an exception occurs, return a generic error response
			GenericResponse<List<User>> errorResponse = new GenericResponse<>("An error occurred", false, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	// Simulated method to fetch users from a database
	private List<User> getUsersFromDatabase() {
		List<User> users = new ArrayList<>();
	
		users.add(new User(1L, "user1", "user1@example.com"));
		users.add(new User(2L, "user2", "user2@example.com"));
		users.add(new User(3L, "user3", "user3@example.com"));
		return users;
	}
}
