package org.dikshit.authserver.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.dikshit.authserver.model.Authorities;
import org.dikshit.authserver.model.Roles;
import org.dikshit.authserver.repository.RolesRepository;
import org.dikshit.authserver.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private UserPrincipalDetailsService userPrincipalDetailsService;
	private UserRepository userRepository;
	private RolesRepository roleRepository;

	
	@Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	public SecurityConfiguration(UserPrincipalDetailsService userPrincipalDetailsService,UserRepository userRepository,RolesRepository roleRepository) {
		this.userPrincipalDetailsService = userPrincipalDetailsService;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(authenticationProvider());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		 http
         // remove csrf and state in session because in jwt we do not need them
         .csrf().disable()
         .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
         .and()
         // add jwt filters (1. authentication, 2. authorization)
         .addFilter(new JwtAuthenticationFilter(authenticationManager()))
         .addFilter(new JwtAuthorizationFilter(authenticationManager(),  this.userRepository))
         .authorizeRequests()
         // configure access rules
         .antMatchers(HttpMethod.POST, "/login").permitAll();
		 
		 http.authorizeRequests().antMatchers("/h2-console/**").permitAll();
		 http.authorizeRequests().antMatchers("/api/public/test/**").permitAll();
		 
		 
		 Map<Authorities,List<String>> authRoleMap = getMatchInfo();
		 
		 authRoleMap.forEach((authority,roleList) ->{
			 
			 String[] roles = roleList.stream().toArray(String[]::new);
			 logger.info("----------------{}, {}, {} ",authority.getEndPoint(),roles,httpMethodType(authority.getRequestType()));
			 try {
				http.authorizeRequests().antMatchers(httpMethodType(authority.getRequestType()),authority.getEndPoint()).hasAnyRole(roles);
			} catch (Exception e) {
				e.printStackTrace();
			}
		 });
		logger.info(http.authorizeRequests().toString());
	}

	private HttpMethod httpMethodType(String method) {
		HttpMethod methodType;
		switch(method) 
        { 
            case "GET": 
            	methodType = HttpMethod.GET;
                break; 
            case "POST": 
            	methodType =  HttpMethod.POST;; 
                break; 
            case "DELETE": 
            	methodType =  HttpMethod.DELETE; 
                break; 
            case "PUT": 
            	methodType =  HttpMethod.PUT; 
                break; 
            default: 
            	methodType =  HttpMethod.GET; 
        } 
		
		return methodType;
	}


	
	//Map of Authorities and Roles name
	@Transactional
	private Map<Authorities,List<String>> getMatchInfo() {

		Map<Authorities,List<String>> authRoleMap = new HashMap<>(); //map of authorities and roles
		
		List<Roles> allRoles = roleRepository.findAll();

		for (Roles role : allRoles) {
			List<Authorities> roleAuth = role.getAuthorities();
			for (Authorities auth : roleAuth) {
				if(authRoleMap.containsKey(auth)){
					authRoleMap.get(auth).add(role.getRoleName());
				}else {
					List<String> authRoles = new ArrayList<String>();
					authRoles.add(role.getRoleName());
					authRoleMap.put(auth, authRoles);
				}
			}
		}

		return authRoleMap;
	}


	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		daoAuthenticationProvider.setUserDetailsService(this.userPrincipalDetailsService);

		return daoAuthenticationProvider;
	}
	
	
}
