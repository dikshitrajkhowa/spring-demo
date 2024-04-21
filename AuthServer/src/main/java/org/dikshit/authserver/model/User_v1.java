package org.dikshit.authserver.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User_v1 {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String password;

	private int active;

	private String roles = "";

	private String permissions = "";

	public User_v1(String username, String password, String roles, String permissions) {
		this.username = username;
		this.password = password;
		this.roles = roles;
		this.permissions = permissions;
		this.active = 1;
	}

	protected User_v1() {
	}

	public long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public int getActive() {
		return active;
	}

	public String getRoles() {
		return roles;
	}

	public String getPermissions() {
		return permissions;
	}

	public List<String> getRoleList() {
		if (this.roles.length() > 0) {
			return Arrays.asList(this.roles.split(","));
		}
		return new ArrayList<>();
	}

	public List<String> getPermissionList() {
		if (this.permissions.length() > 0) {
			return Arrays.asList(this.permissions.split(","));
		}
		return new ArrayList<>();
	}
}
