package com.Local.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.Local.api.service.consumerService;
import com.Local.api.entities.*;

@RestController
public class consumerController {

	
	@Autowired
	private consumerService service;
	
	@GetMapping("/consumers")
	public List<consumerdetails> findAllConsumers(){
		
		return service.findAllConsumers();
	}
	@GetMapping("/")
	public String status(){
		
		return "Im up!!";
	}
	
	@PostMapping("/updateLocation")
	public ResponseEntity<String> changeLocation(@RequestBody changeLocation location) {
		return service.changeLocation(location);
	}
}
