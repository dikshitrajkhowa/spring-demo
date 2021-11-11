package org.dikshit.SpringBootSecurity1.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.dikshit.SpringBootSecurity1.model.Authorities;
import org.dikshit.SpringBootSecurity1.model.Roles;
import org.dikshit.SpringBootSecurity1.repository.RolesRepository;
import org.dikshit.SpringBootSecurity1.repository.UserRepository;
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

//	static List<MatcherInfo> creds = getInfo(); //This should ideally be generated from prod
	
	@Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	public SecurityConfiguration(UserPrincipalDetailsService userPrincipalDetailsService,UserRepository userRepository,RolesRepository roleRepository) {
		this.userPrincipalDetailsService = userPrincipalDetailsService;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

//	static List<MatcherInfo> getInfo() {
//
//		List<MatcherInfo> creds = new ArrayList<>();
//		creds.add(new MatcherInfo("/api/public/management/*", "MANAGER"));
//		creds.add(new MatcherInfo("/api/public/admin/*", "ADMIN")); 
//		return creds;
//	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(authenticationProvider());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		getMatchInfo();
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
		 
//		 http.authorizeRequests().antMatchers("/api/public/management/*").hasRole("MANAGER")
//         .antMatchers("/api/public/admin/*").hasRole("ADMIN")
//         .anyRequest().authenticated();
		 
//		 http.authorizeRequests().antMatchers("/api/public/management/*").hasRole("MANAGER")
//       .antMatchers(HttpMethod.POST,"/api/public/admin/*").hasRole("ADMIN")
//       .anyRequest().authenticated();
		 
//		 for(MatcherInfo info:creds) {
//			 logger.info(info.getApi() +" -> "+ info.getRole());
//			 http.authorizeRequests().antMatchers(info.getApi()).hasRole(info.getRole()).anyRequest().authenticated();
//		 }
		 
		 //Wont work due to order problem, if same auth is mapped to more than 1 role then only last role is considered
//		 List<MatcherInfo> matchers = getMatchInfo();
//		 for(MatcherInfo info:matchers) {
//			 http.authorizeRequests().antMatchers(info.getApi()).hasRole(info.getRole());
////			 http.authorizeRequests().antMatchers(info.getApi()).hasRole(info.getRole()).anyRequest().authenticated();
//		 }
		 
		 Map<Authorities,List<String>> authRoleMap = getMatchInfo();
		 
		 authRoleMap.forEach((authority,roleList) ->{
			 
//			 String roles = String.join(",", roleList); //convert the list of role names to comma separated string
			 String[] roles = roleList.stream().toArray(String[]::new);
			 logger.info("----------------{}, {}, {} ",authority.getEndPoint(),roles,httpMethodType(authority.getRequestType()));
			 try {
				http.authorizeRequests().antMatchers(httpMethodType(authority.getRequestType()),authority.getEndPoint()).hasAnyRole(roles);
//				http.authorizeRequests().antMatchers(httpMethodType(authority.getRequestType()),authority.getEndPoint()).hasAnyRole(roles);
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


	
//	@Transactional
//	private List<MatcherInfo> getMatchInfo() {
//		List<MatcherInfo> matchers = new ArrayList<MatcherInfo>();
//
//		List<Roles> allRoles = roleRepository.findAll();
//
//		for (Roles role : allRoles) {
//			List<Authorities> roleAuth = role.getAuthorities();
//			for (Authorities auth : roleAuth) {
//				MatcherInfo m = new MatcherInfo(auth.getEndPoint(), role.getRoleName());
//				matchers.add(m);
//			}
//		}
//
//		return matchers;
//	}
	
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

	/*
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
         .antMatchers(HttpMethod.POST, "/login").permitAll()
         .antMatchers("/h2-console/**").permitAll()
         .antMatchers("/api/public/management/*").hasRole("MANAGER")
         .antMatchers("/api/public/admin/*").hasRole("ADMIN")
         .anyRequest().authenticated();
	}
	*/
	

	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		daoAuthenticationProvider.setUserDetailsService(this.userPrincipalDetailsService);

		return daoAuthenticationProvider;
	}
	
	
}
