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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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

	
	/**
	 * Form based authentication
	 */
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/index.html").permitAll().antMatchers("/profile/index").authenticated()
				.antMatchers("/admin/index").hasRole("ADMIN")
				.antMatchers("/management/index").hasAnyRole("ADMIN", "MANAGER")
				.antMatchers("/api/public/test1").hasAuthority("ACCESS_TEST1")
				.antMatchers("/api/public/test2").hasAuthority("ACCESS_TEST2")
				.antMatchers("/api/public/users")
				.hasRole("ADMIN")
				.and()
				.formLogin()
				.loginProcessingUrl("/signin") //we dont need to implement this, Springboot will automatically do, default is /login
                .loginPage("/login").permitAll()
                .usernameParameter("txtUsername") //default is username i.e. if we use username in login.html then we dont need this customization here
                .passwordParameter("txtPassword") //default is password, 
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login")
                .and()
                .rememberMe().tokenValiditySeconds(2592000).key("mySecret!").rememberMeParameter("checkRememberMe");

		// for h2-console
		http.csrf().disable();
		http.headers().frameOptions().disable();
	}

	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
