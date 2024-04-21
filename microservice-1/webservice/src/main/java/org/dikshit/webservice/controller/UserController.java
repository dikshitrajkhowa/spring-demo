package org.dikshit.webservice.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

	@GetMapping
	public String getUser() {
		return "Get user was called";

	}

	@GetMapping(path="/{userId}")
	public String getUserById(@PathVariable String userId) {
		return "User with id = "+userId +" retrieved";
		
	}

	@PostMapping
	public String createUser() {
		return "Create user was called";

	}

	@PutMapping
	public String updateUser() {
		return "Update user was called";

	}

	@DeleteMapping
	public String delUser() {
		return "Delete user was called";

	}

}
