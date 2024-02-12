package com.Local.api.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import 	 com.Local.api.model.changeLocation;
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
	
	public ResponseEntity<String>  changeLocation(changeLocation location){
		
		try {
		consumerdetails consumer  =  repo.findById(location.id).get();
		consumer.consumer_location = location.newLocation;
		repo.save(consumer);
		
		
		return ResponseEntity.ok("Successfull") ;
		}catch(Exception e) {
			
		}
		
		return (ResponseEntity<String>) ResponseEntity.badRequest();
	}
	public consumerdetails save(consumerdetails newConsumer) {
		repo.save(newConsumer);
		return newConsumer;
	}
	public String getDate() {
		 LocalDate currentDate = LocalDate.now();

	        // Define the format you want for the date string
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	        // Format the current date using the defined formatter
	        String dateString = currentDate.format(formatter);
	      
	        return dateString;
	}
	public String generateId() {
		String consumerId = null;
		  String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	       StringBuilder uniqueString = new StringBuilder();

	       Random random = new Random();
	       for (int i = 0; i < 10; i++) {
	           int index = random.nextInt(characters.length());
	           uniqueString.append(characters.charAt(index));
	       }
	       consumerId = uniqueString.toString();
	       return consumerId;
	}
}
