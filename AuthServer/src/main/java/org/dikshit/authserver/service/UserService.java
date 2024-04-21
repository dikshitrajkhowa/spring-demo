package org.dikshit.authserver.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.dikshit.authserver.model.User;
import org.dikshit.authserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Transactional
public class UserService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	EntityManager em;
	
	
	public List<User> getAllUsers(){
//		List<User> findAllUsers = userRepository.findAllUsers();
//		return findAllUsers;
		
		return userRepository.findAll();
	}
}
