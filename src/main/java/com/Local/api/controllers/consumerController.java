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
	
	@PutMapping(path = "/updateLocation/{id}/{location}" , consumes = "application/json")
	public ResponseEntity<String> changeLocation(@PathVariable("id") String id, @PathVariable("location") String location) {
		
		 changeLocation locationObj = new changeLocation();
		 locationObj.id = id;
		 locationObj.newLocation = location;
		 
		return service.changeLocation(locationObj);
	}
}
