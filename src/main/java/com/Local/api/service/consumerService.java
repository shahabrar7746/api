package com.Local.api.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import 	 com.Local.api.model.changeLocation;
import com.Local.api.model.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;

import com.Local.api.Exceptions.customError;
import com.Local.api.entities.consumerdetails;
import com.Local.api.repository.consumerRepo;


@Service
public class consumerService {

	@Autowired
	 private JavaMailSender emailSender;
	
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
	public consumerdetails save(consumerdetails newConsumer) throws customError {
		consumerdetails consumer = repo.findByemail(newConsumer.email);
		if(consumer != null) {
			throw new customError("Email already in use",HttpStatus.NOT_ACCEPTABLE);
		}
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
	private String getOtp() {
		Random rand = new Random();
		String num = "1234567890";
		String otp = "";
		for(int i = 0;i<6;i++) {
			otp = otp + rand.nextInt(num.length());
		}
		return otp;
	}
	public ResponseEntity<String> sendMail(String email) throws customError{
		String otp = getOtp();
		
		consumerdetails consumer = repo.findByemail(email);
		if(consumer == null) {
			throw new customError("invalid or incorrect email", HttpStatus.BAD_REQUEST);
		}
		  SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(email);
	        
	        message.setSubject("OTP for resetting your 24Local Private Limited account password\"");
	        message.setText("Hi Dear User Your OTP for 24Local Private Limited is "  + otp + 
	        		      				"\nPlease enter this code in the app or on the website to verify your identity.\n"
	        					+ "This OTP is valid for 10 minutes. If you do not use it within that time, a new OTP will be generated.\n"
	        								+ "\n"
	        		       						+ "If you did not request this OTP, please ignore this email.\n"
	        		        						+ "Thanks, \n" +
	        		       						"The 24Local Team");
	        emailSender.send(message);
	       
			return ResponseEntity.ok(otp);
	}
	
	public ResponseEntity<String> doLogin(login logUser) throws customError{
		consumerdetails consumer = repo.findByemail(logUser.emailORnumber);
		if(consumer == null) {
			throw new customError("No User found",HttpStatus.BAD_GATEWAY);
		}
		return  ResponseEntity.ok(consumer.consumer_id);
	}
}
