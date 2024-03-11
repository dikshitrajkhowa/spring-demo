package org.dikshit.limitsservice.controller;

import org.dikshit.limitsservice.configuration.Config;
import org.dikshit.limitsservice.service.beans.Limits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LimitsController {
	
	@Autowired
	private Config config;
	
	@GetMapping("/limits")
	public Limits retrieveLimits() {
		return new Limits(config.getMinimum(),config.getMaximum());
//		return new Limits(1,1000);
	}

}
