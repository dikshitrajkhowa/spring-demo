package org.dikshit.SpringBootSecurity1.repository;

import org.dikshit.SpringBootSecurity1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
