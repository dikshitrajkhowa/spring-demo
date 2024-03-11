package org.dikshit.SpringBootSecurity1.repository;

import org.dikshit.SpringBootSecurity1.model.Authorities;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthoritiesRepository extends JpaRepository<Authorities, Long> {
	
	Authorities findByName(String name);
}
