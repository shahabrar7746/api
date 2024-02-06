package com.Local.api.service;

import java.util.List;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.Local.api.entities.consumerdetails;
import com.Local.api.repository.consumerRepo;


@Service
public class consumerService {

	
	
	@Autowired
	private consumerRepo repo;
	
	
	
	public List<consumerdetails> findAllConsumers(){
		return repo.findAll();
	}
	
	public ResponseEntity<String> changeLocation(com.Local.api.entities.changeLocation location){
		
		try {
		consumerdetails consumer  =  repo.findById(location.id).get();
		consumer.consumer_location = location.newLocation;
		repo.save(consumer);
		
		return ResponseEntity.ok("Successfull");
		}catch(Exception e) {
			
		}
		
		return (ResponseEntity<String>) ResponseEntity.badRequest();
	}
}
