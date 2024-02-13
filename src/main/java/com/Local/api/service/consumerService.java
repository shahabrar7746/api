package com.Local.api.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.Local.api.model.otp;
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
import com.Local.api.entities.jwt;
import com.Local.api.Exceptions.customError;
import com.Local.api.entities.consumerdetails;
import com.Local.api.repository.consumerRepo;
import com.Local.api.repository.jwtRepo;


@Service
public class consumerService implements consumerInterface{

	@Autowired
	 private JavaMailSender emailSender;
	
	@Autowired
	consumerRepo repo;
	@Autowired
	private jwtRepo jwt;
	
	
	private final jwt token = new jwt();
	
	

	private final otp res = new otp();
	private final String num = "1234567890";
private final String chars = "1234567890ABQWERTYUIOPSDFGHJKLZXCVNM";
   private static final String TOKKEN_EXPIRED = "Session expired do Log in again"; 
	private final Random rand = new Random();
	private static final String EMAIL_NOT_FOUND = "Email not found";
	private static final String USER_NOT_FOUND = "User not found";
	private static final String INCORRECT_PASSWORD = "Incorrect or invalid password";
	private static final String EMAIL_IN_USE = "Email already in use";
	private static final String SUCCESS = "Successfull";
	private final  String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

	public List<consumerdetails> findAllConsumers(){
		return repo.findAll();
	}
	
	public ResponseEntity<String>  changeLocation(changeLocation location) throws customError{
		
		jwt token = jwt.findById(location.token).get();
		if(token == null) {
			throw new customError(TOKKEN_EXPIRED,HttpStatus.GATEWAY_TIMEOUT);

		}
		consumerdetails consumer  =  repo.findById(token.id).get();
		if(consumer == null) {
			throw new customError(EMAIL_NOT_FOUND,HttpStatus.NOT_FOUND);
		}
		consumer.consumer_location = location.newLocation;
		repo.save(consumer);
		
		
		return ResponseEntity.ok(SUCCESS) ;
		
	}
	public consumerdetails register(consumerdetails newConsumer) throws customError {
		newConsumer.consumer_id = generateId();
		newConsumer.registration_date = getDate();
		consumerdetails consumer = repo.findByemail(newConsumer.email);
		if(consumer != null) {
			throw new customError(EMAIL_IN_USE,HttpStatus.CONFLICT);
		}
		repo.save(newConsumer);
		return newConsumer;
	}
	private String getDate() {
		 LocalDate currentDate = LocalDate.now();

	        // Define the format you want for the date string
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	        // Format the current date using the defined formatter
	        String dateString = currentDate.format(formatter);
	      
	        return dateString;
	}
	public String generateId() {
		String consumerId = null;
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
	public ResponseEntity<otp> sendMail(String email) throws customError{
		String otp = getOtp();
		
		consumerdetails consumer = repo.findByemail(email);
		if(consumer == null) {
			throw new customError(EMAIL_NOT_FOUND, HttpStatus.BAD_REQUEST);
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
	       res.otp = otp;
	       res.token = generateToken();
	       token.expiry = getTime();
	       token.id = consumer.consumer_id;
	       token.token = res.token;
	       token.type = "AUTH";
	       jwt.save(token);
	       deleteToken();
			return ResponseEntity.ok(res);
	}
	
	public ResponseEntity<String> doLogin(login logUser) throws customError{
		consumerdetails consumer = repo.findByemail(logUser.emailORnumber);
		if(consumer == null) {
			
			throw new customError(USER_NOT_FOUND,HttpStatus.NOT_FOUND);
		}
		if(!consumer.password.equals(logUser.password)) {
			throw new customError(INCORRECT_PASSWORD,HttpStatus.NOT_ACCEPTABLE);

		}
		
	
		token.expiry = getTime();
		token.id = consumer.consumer_id;
		token.token = generateToken();
		
		jwt.save(token);
		
		
		deleteToken();
		return  ResponseEntity.ok(token.token);
	}
	private String generateToken()
	{
		String token = "";
		
		
		
		for(int i = 0;i<40;i++) {
			token = token + chars.charAt(rand.nextInt(chars.length()));
		}
		return token;
	}
	private void deleteToken() {
		List<jwt> tokens = jwt.findAll();
	  for(int i =0;i<tokens.size();i++) {
		  jwt token = tokens.get(i);
		  String hour = token.expiry.substring(0,2);
		  String currentHour = getTime().substring(0,2);
		  if(Integer.parseInt(hour) - Integer.parseInt(currentHour) != 0) {
			  jwt.delete(token);
		  }
		  
	  }
	}
	private String getTime() {
		  LocalDateTime currentTime = LocalDateTime.now();

	        // Define a format for the time (optional)
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	        // Format the current time using the specified format
	        String formattedTime = currentTime.format(formatter);
	        return formattedTime;
	}
}
