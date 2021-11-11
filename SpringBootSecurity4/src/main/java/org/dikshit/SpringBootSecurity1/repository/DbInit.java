package org.dikshit.SpringBootSecurity1.repository;

import java.util.Arrays;
import java.util.List;

import org.dikshit.SpringBootSecurity1.model.Authorities;
import org.dikshit.SpringBootSecurity1.model.Roles;
import org.dikshit.SpringBootSecurity1.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//Run when necessary
@Service
public class DbInit implements CommandLineRunner {

	private UserRepository userRepository;
	private RolesRepository roleRepository;
	private PasswordEncoder passwordEncoder;
	private AuthoritiesRepository authoritiesRepository;

	public DbInit(UserRepository userRepository, PasswordEncoder passwordEncoder,RolesRepository roleRepository,AuthoritiesRepository authoritiesRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
		this.authoritiesRepository = authoritiesRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		
		boolean addData = false; //truncate db before runnning
		if(addData) {
			insertData();	
		}
		
		
		//OLD VERSION		
//		this.userRepository.deleteAll();
//
//		User dan = new User("dan", passwordEncoder.encode("dan123"), "USER", "");
//		User admin = new User("admin", passwordEncoder.encode("admin123"), "ADMIN", "ACCESS_TEST1,ACCESS_TEST2");
//		User manager = new User("manager", passwordEncoder.encode("manager123"), "MANAGER", "ACCESS_TEST1");
//
//		List<User> users = Arrays.asList(dan, admin, manager);
//
//		// Save to db
//		this.userRepository.saveAll(users);
		
	}

	private void insertData() {
		//Add Authorities
		this.authoritiesRepository.deleteAll();
		Authorities a1 = new Authorities("Admin", "/api/public/admin/*",true,"GET");
		Authorities a2 = new Authorities("Manager", "/api/public/management/*",true,"GET");
		Authorities a3 = new Authorities("Public", "/api/public/test/*",true,"GET");
		Authorities a4 = new Authorities("ParamView", "/api/public/param/{param:[a-zA-Z0-9]+}",true,"GET");
		Authorities a5 = new Authorities("ParamPost", "/api/public/param/{param:[a-zA-Z0-9]+}",true,"POST");
		
		List<Authorities> aths = Arrays.asList(a1,a2,a3,a4,a5);
		authoritiesRepository.saveAll(aths);
		
		
		//Add Roles & corresponding authorities
		roleRepository.deleteAll();
		Roles r1 = new Roles("ADMIN", "Admin Role");
		Roles r2 = new Roles("MANAGER", "Manager Role");
		Roles r3 = new Roles("PUBLIC", "Public Role");
		
		r1.addAuthorities(authoritiesRepository.findByName("Admin"));
		r1.addAuthorities(authoritiesRepository.findByName("Public"));
		r1.addAuthorities(authoritiesRepository.findByName("ParamView"));
		r1.addAuthorities(authoritiesRepository.findByName("ParamPost"));
		
		r2.addAuthorities(authoritiesRepository.findByName("Manager"));
		r2.addAuthorities(authoritiesRepository.findByName("ParamView"));
		r3.addAuthorities(authoritiesRepository.findByName("Public"));
		
		List<Roles> rolesList = Arrays.asList(r1,r2,r3);
		roleRepository.saveAll(rolesList);
//		
//		//Add Users
		this.userRepository.deleteAll();
		User dan = new User("dan", passwordEncoder.encode("dan123"),1);
		User adminUsr = new User("admin", passwordEncoder.encode("admin123"),1);
		User managerUsr = new User("manager", passwordEncoder.encode("manager"),1);
		
		Roles adminRole = roleRepository.findByRoleName("Admin");
		Roles mgrRole = roleRepository.findByRoleName("Manager");
		Roles publicRole = roleRepository.findByRoleName("Public");
		
		dan.setRole(publicRole);
		adminUsr.setRole(adminRole);
		managerUsr.setRole(mgrRole);
		
		List<User> users = Arrays.asList(dan, adminUsr, managerUsr);
		userRepository.saveAll(users);
	}

}
