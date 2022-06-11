package org.dikshit.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class ConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
	}

}

/*

This config server will connect to some repo like git to pick properties files
Various microservices will connect to the config server to retrieve the properties 
stored in the git repo.
So basically they dont have to save the properties locally

Refer to application.properties to check how it connects to git repo

spring.cloud.config.server.git.uri=file:///C:/Dikshit/git-config-local-repo

http://localhost:8888/limits-service/default
*/
