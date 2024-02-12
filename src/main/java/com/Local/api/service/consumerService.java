package com.Local.api.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import 	 com.Local.api.model.changeLocation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;

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
	private String getOtp() {
		Random rand = new Random();
		String num = "1234567890";
		String otp = "";
		for(int i = 0;i<4;i++) {
			otp = otp + rand.nextInt(num.length());
		}
		return otp;
	}
	public String sendMail(String email) {
		String otp = getOtp();
		
		consumerdetails consumer = repo.findByemail(email);
		if(consumer == null) {
			return "incorrect email";
		}
		  SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(email);
	        
	        message.setSubject("OTP for resetting your 24Local Private Limited account password\"");
	        message.setText("Hi \" + Dear User +\",\\n\"\r\n"
	        		+ "        		+ \"Your OTP for 24Local Private Limited is \" + "+ otp + " + \" \\n\"\r\n"
	        		+ "        				+ \"Please enter this code in the app or on the website to verify your identity.\\n\" + \"\"\r\n"
	        		+ "        						+ \"This OTP is valid for 10 minutes. If you do not use it within that time, a new OTP will be generated.\\r\\n\"\r\n"
	        		+ "        						+ \"\\r\\n\"\r\n"
	        		+ "        						+ \"If you did not request this OTP, please ignore this email.\\n\"\r\n"
	        		+ "        						+\"Thanks,\\n\"\r\n"
	        		+ "        						+ \"The 24Local Team\"");
	        return otp;
	}
}
