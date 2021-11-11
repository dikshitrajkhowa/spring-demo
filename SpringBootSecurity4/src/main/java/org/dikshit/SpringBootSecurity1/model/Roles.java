package org.dikshit.SpringBootSecurity1.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Roles {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false)
	private String roleName;
	
	@Column
	private String roleDescription;
	
	@OneToMany(mappedBy = "role")
	private List<User> users;
	
	//Only services entered in this table are picked by security
	//If some service is not included in this table then it is not secured
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "AUTH_ROLES", joinColumns = @JoinColumn(name = "ROLE_ID"), inverseJoinColumns = @JoinColumn(name = "AUTHORITY_ID"))
	private List<Authorities> authorities = new ArrayList<>();
	
	public Roles() {
	}

	public Roles(String roleName, String roleDescription) {
		this.roleName = roleName;
		this.roleDescription = roleDescription;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDescription() {
		return roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Authorities> getAuthorities() {
		return authorities;
	}

	public void addAuthorities(Authorities authority) {
		this.authorities.add(authority);
	}

	@Override
	public String toString() {
		return "Roles [roleName=" + roleName + "]";
	}
	
	
	
}
