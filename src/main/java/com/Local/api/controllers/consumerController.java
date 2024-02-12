package com.Local.api.controllers;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.Local.api.service.consumerService;
import com.Local.api.entities.*;
import 	 com.Local.api.model.changeLocation;
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
	
	
	
	@PostMapping(path = "/register/consumer")
	public consumerdetails register(@RequestBody consumerdetails newConsumer) {
		newConsumer.consumer_id = service.generateId();
		newConsumer.registration_date = service.getDate();
	    return service.save(newConsumer);	
	}
	
	
	@PutMapping("/changeLocation")
	public ResponseEntity<String> updateLocation(@RequestBody changeLocation newLocation){
		return service.changeLocation(newLocation);
	}
	
	
}
