package org.dikshit.SpringBootSecurity1.repository;

import java.util.List;

import org.dikshit.SpringBootSecurity1.model.Roles;
import org.dikshit.SpringBootSecurity1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

//@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	User findByUsername(String username);
	Roles findByRole(Roles role);
	
	@Query("Select u from User u")
	List<User> findAllUsers();
	
}
