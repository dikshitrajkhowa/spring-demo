package org.dikshit.SpringBootSecurity1.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	/*
	 * Configuring in-memory user credentials with Permission based authorizations
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
				.withUser("admin")
				.password(passwordEncoder().encode("admin"))
				.roles("ADMIN").authorities("ACCESS_TEST1","ACCESS_TEST2")
		.and().withUser("dan").password(passwordEncoder().encode("dan")).roles("USER")
		.and().withUser("manager").password(passwordEncoder().encode("manager"))
		.roles("MANAGER").authorities("ACCESS_TEST1");
	}
	
	/**
	 * ROLE BASED AUTHORIZATION Example
	 * Configure HttpBasic for all specific requests with 
	 * specific roles using antMatchers
	 * Roles are defined in the above method
	 */
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/index.html").permitAll()
			.antMatchers("/profile/index").authenticated()
			.antMatchers("/admin/index").hasRole("ADMIN")
			.antMatchers("/management/index").hasAnyRole("ADMIN","MANAGER")
			.antMatchers("/api/public/test1").hasAuthority("ACCESS_TEST1")
			.antMatchers("/api/public/test2").hasAuthority("ACCESS_TEST2")
			.and()
			.httpBasic();
	}
	

	/**
	 * Configure HttpBasic for all requests
	 */
//	protected void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests().anyRequest().authenticated().and().httpBasic();
//	}
	
	//We need a password encoder, else will get "There is no PasswordEncoder mapped for the id "null"
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
