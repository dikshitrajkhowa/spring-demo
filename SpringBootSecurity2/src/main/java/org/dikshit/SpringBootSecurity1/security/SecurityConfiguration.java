package org.dikshit.SpringBootSecurity1.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserPrincipalDetailsService userPrincipalDetailsService;

	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		daoAuthenticationProvider.setUserDetailsService(this.userPrincipalDetailsService);

		return daoAuthenticationProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(authenticationProvider());
	}

	/*
	 * Configuring in-memory user credentials with Permission based authorizations
	 */
	/**
	 * @Override protected void configure(AuthenticationManagerBuilder auth) throws
	 *           Exception { auth.inMemoryAuthentication() .withUser("admin")
	 *           .password(passwordEncoder().encode("admin"))
	 *           .roles("ADMIN").authorities("ACCESS_TEST1","ACCESS_TEST2","ROLE_ADMIN")
	 *           //ROLE is also an authority. Authorities take precedence over role,
	 *           so when we have both roles and authorities we need to add the role
	 *           with ROLE_ as prefix in the list of authorities
	 *           .and().withUser("dan").password(passwordEncoder().encode("dan")).roles("USER")
	 *           .and().withUser("manager").password(passwordEncoder().encode("manager"))
	 *           .roles("MANAGER").authorities("ACCESS_TEST1","ROLE_MANAGER"); }
	 */

	/**
	 * ROLE BASED AUTHORIZATION Example Configure HttpBasic for all specific
	 * requests with specific roles using antMatchers Roles are defined in the above
	 * method
	 */
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/index.html").permitAll().antMatchers("/profile/index").authenticated()
				.antMatchers("/admin/index").hasRole("ADMIN").antMatchers("/management/index")
				.hasAnyRole("ADMIN", "MANAGER").antMatchers("/api/public/test1").hasAuthority("ACCESS_TEST1")
				.antMatchers("/api/public/test2").hasAuthority("ACCESS_TEST2").antMatchers("/api/public/users")
				.hasRole("ADMIN").and().httpBasic();

		// for h2-console
		http.csrf().disable();
		http.headers().frameOptions().disable();
	}

	/**
	 * Configure HttpBasic for all requests
	 */
//	protected void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests().anyRequest().authenticated().and().httpBasic();
//	}

	// We need a password encoder, else will get "There is no PasswordEncoder mapped
	// for the id "null"
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// //ROLE is also an authority. Authorities take precedence over role, so when
	// we have both roles and authorities we need to add the role with ROLE_ as
	// prefix in the list of authorities
}
