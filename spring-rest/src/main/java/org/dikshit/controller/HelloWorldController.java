package org.dikshit.controller;

import java.util.Locale;

import org.dikshit.model.HelloWorldBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

	@Autowired
	private MessageSource messageSource;

	@GetMapping(path = "/hello-world")
	public String helloWorld() {
		return "Hello World";
	}

	@GetMapping(path = "/hello-world-bean")
	public HelloWorldBean helloWorldBean() {
		return new HelloWorldBean("Hello World");
	}

	/// hello-world/path-variable/in28minutes
	@GetMapping(path = "/hello-world/path-variable/{name}")
	public HelloWorldBean helloWorldPathVariable(@PathVariable String name) {
		return new HelloWorldBean(String.format("Hello World, %s", name));
	}
	
	//Internationalizations
	//User should pass in request header , the accept language
	
	@GetMapping(path = "/hello-world-internationalized")
//	public String helloWorldInternationalized(@RequestHeader(name = "Accept-Language",required=false) Locale locale) {
//		return messageSource.getMessage("good.morning.message", null, "Default message",locale);
//	}
	
	//Instead of pass the Locale in the parameter we can use LocalContextHolder
	public String helloWorldInternationalized(@RequestHeader(name = "Accept-Language",required=false) Locale locale) {
		return messageSource.getMessage("good.morning.message", null, "Default message",LocaleContextHolder.getLocale());
	}
}

/**
 * For XML we need the dependency in POM and in the request header we should send Accept=application/xml
 */
