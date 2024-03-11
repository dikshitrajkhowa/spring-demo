package com.example.demo.pkg1;

public class User {
	private Long id;
	private String username;
	private String email;

	public User() {
	}

	public User(long l, String string, String string2) {

		this.id = l;
		this.username = string;
		this.email = string2;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
