package org.dikshit.authserver.controller;

import java.util.List;

import org.dikshit.authserver.model.User;
import org.dikshit.authserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/public/")
public class PublicRestApiController {

	@Autowired
	UserService userService;

	public PublicRestApiController() {
	}

	// Available to all authenticated users
	@GetMapping("test")
	public String test1() {
		return "API Test";
	}

	// Available to managers
	@GetMapping("management/test2")
	public String test2() {
		return "API Test 2";
	}

	// Available to ROLE_ADMIN
	@GetMapping("admin/users")
	public List<User> users() {
		List<User> allUsers = userService.getAllUsers();
		
//		long roleId = allUsers.get(0).getRole().getId();
		return allUsers;
	}

	//This is available to both Manager and Admin
	@GetMapping("param/{param}")
//	@GetMapping("param/")
	public String test3(@PathVariable String param) {
//	public String test3() {
		return "Done";
	}
	
	//This is available to Admin only
	@PostMapping("param/{param}")
//	@PostMapping("param/")
	public String test4(@PathVariable String param) {
//	public String test4() {
		return "Done";
	}
}