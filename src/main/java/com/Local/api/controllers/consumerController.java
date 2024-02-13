package com.Local.api.controllers;


import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.Local.api.model.login;
import com.Local.api.model.otp;
import com.Local.api.service.consumerService;

import ch.qos.logback.classic.Logger;

import com.Local.api.Exceptions.customError;
import com.Local.api.entities.*;
import com.Local.api.model.Password;
import 	 com.Local.api.model.changeLocation;
@RestController
public class consumerController {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(consumerController.class);

	@Autowired
	private consumerService service;
	
	@GetMapping("/consumers")
	public List<consumerdetails> findAllConsumers(){
		
		return service.findAllConsumers();
	}
	@GetMapping("/")
	public String status(){
		logger.info("In up status");
		return "Im up!!";
	}
	
	
	
	@PostMapping(path = "/register/consumer")
	public ResponseEntity<String> register(@RequestBody consumerdetails newConsumer) throws customError {
		
	    return service.register(newConsumer);	
	}
	
	
	@PutMapping("/changeLocation")
	public ResponseEntity<String> updateLocation(@RequestBody changeLocation newLocation) throws customError{
		return service.changeLocation(newLocation);
	}
	
	
	@GetMapping("/resetPassword/{email}")
	public ResponseEntity<String> reset(@PathVariable("email") String email) throws customError {
		return service.sendMail(email);
	}
	
	@GetMapping("/login")
	public ResponseEntity<String> login(@RequestBody login logUser) throws customError{
		return service.doLogin(logUser);
	}
	@PutMapping("/otp/verify")
	public ResponseEntity<String> verify(@RequestBody otp obj) throws customError{
		return service.verify(obj);
	}
	
	@PutMapping("/newPassword")
	public ResponseEntity<String> update(@RequestBody Password obj) throws customError{
		return service.update(obj);
	}
	
}
