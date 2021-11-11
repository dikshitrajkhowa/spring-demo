package org.dikshit.SpringBootSecurity1.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Authorities {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false)
	private String name;
	
	private String endPoint;
	
	@ManyToMany(mappedBy = "authorities")
	private List<Roles> roles = new ArrayList<>();
	
	private boolean isSecured;
	
	private String requestType; //GET,POST etc

	public Authorities() {
	}


	public Authorities(String name, String endPoint, boolean isSecured,String requestType) {
		super();
		this.name = name;
		this.endPoint = endPoint;
		this.isSecured = isSecured;
		this.requestType = requestType;
	}



	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public List<Roles> getRoles() {
		return roles;
	}

	public void setRoles(List<Roles> roles) {
		this.roles = roles;
	}


	public boolean isSecured() {
		return isSecured;
	}


	public void setSecured(boolean isSecured) {
		this.isSecured = isSecured;
	}


	public String getRequestType() {
		return requestType;
	}


	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endPoint == null) ? 0 : endPoint.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + (isSecured ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((requestType == null) ? 0 : requestType.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Authorities other = (Authorities) obj;
		if (endPoint == null) {
			if (other.endPoint != null)
				return false;
		} else if (!endPoint.equals(other.endPoint))
			return false;
		if (id != other.id)
			return false;
		if (isSecured != other.isSecured)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (requestType == null) {
			if (other.requestType != null)
				return false;
		} else if (!requestType.equals(other.requestType))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Authorities [name=" + name + ", endPoint=" + endPoint + "]";
	}
	
	
}
