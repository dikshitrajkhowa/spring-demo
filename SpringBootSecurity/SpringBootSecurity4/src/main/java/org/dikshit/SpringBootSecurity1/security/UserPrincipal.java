package org.dikshit.SpringBootSecurity1.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.dikshit.SpringBootSecurity1.model.Authorities;
import org.dikshit.SpringBootSecurity1.model.Roles;
import org.dikshit.SpringBootSecurity1.model.User;
import org.dikshit.SpringBootSecurity1.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

public class UserPrincipal implements UserDetails {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	
	private User user;
	private Roles role;
	

    public UserPrincipal(User user){
        this.user = user;
    }


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		List<Authorities> usrAuthorities = user.getRole().getAuthorities();
		
		// Extract list of permissions (name)
		usrAuthorities.forEach(p -> {
            GrantedAuthority authority = new SimpleGrantedAuthority(p.getEndPoint());
            authorities.add(authority);
        });
		
		 // Extract list of roles (ROLE_name). A user is currently mapped to only one role though
		
		GrantedAuthority authorityRole = new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName());
		authorities.add(authorityRole);
		
		
		
//		List<GrantedAuthority> authorities = new ArrayList<>();
		
		// Extract list of permissions (name)
//        this.user.getPermissionList().forEach(p -> {
//            GrantedAuthority authority = new SimpleGrantedAuthority(p);
//            authorities.add(authority);
//        }); 
        
        // Extract list of roles (ROLE_name)
//        this.user.getRoleList().forEach(r -> {
//            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + r);
//            authorities.add(authority);
//        });
        
		return authorities;
	}

	@Override
	public String getPassword() {
		return this.user.getPassword();
	}

	@Override
	public String getUsername() {
		 return this.user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		 return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		 return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		 return true;
	}

	@Override
	public boolean isEnabled() {
		 return this.user.getActive() == 1;
	}

	public Roles getRole() {
		return role;
	}

	public void setRole(Roles role) {
		this.role = role;
	}

	
}
