package org.dikshit.authserver.repository;

import org.dikshit.authserver.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles, Long> {
	Roles findByRoleName(String roleName);
}
