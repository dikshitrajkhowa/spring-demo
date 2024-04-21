package org.dikshit.authserver.repository;

import org.dikshit.authserver.model.Authorities;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthoritiesRepository extends JpaRepository<Authorities, Long> {
	
	Authorities findByName(String name);
}
