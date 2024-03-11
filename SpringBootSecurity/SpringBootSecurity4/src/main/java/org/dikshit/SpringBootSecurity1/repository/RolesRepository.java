package org.dikshit.SpringBootSecurity1.repository;

import org.dikshit.SpringBootSecurity1.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles, Long> {
	Roles findByRoleName(String roleName);
}
