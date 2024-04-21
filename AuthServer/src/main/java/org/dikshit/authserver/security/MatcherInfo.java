package org.dikshit.authserver.security;

import java.util.List;

//For testing purpose
public class MatcherInfo {

	private String api;
	private String role;
	private List<String> roles;

	
	public MatcherInfo(String api, String role) {
		this.api = api;
		this.role = role;
	}

	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	
	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "MatcherInfo [api=" + api + ", role=" + role + "]";
	}

	
}
