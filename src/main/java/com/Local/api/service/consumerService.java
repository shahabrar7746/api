package com.Local.api.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.Local.api.model.otp;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import com.Local.api.model.Password;
import 	 com.Local.api.model.changeLocation;
import com.Local.api.model.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;
import com.Local.api.entities.jwt;
import com.Local.api.entities.otp_storage;

import com.Local.api.Exceptions.customError;
import com.Local.api.entities.consumerdetails;
import com.Local.api.repository.consumerRepo;
import com.Local.api.repository.jwtRepo;
import com.Local.api.repository.otpRepo;


@Service
public class consumerService implements consumerInterface{

	@Autowired
	 private JavaMailSender emailSender;
	
	@Autowired
	consumerRepo repo;
	@Autowired
	private jwtRepo jwt_repo;
	
	@Autowired
	private otpRepo repo_otp;
	
	private final jwt token = new jwt();
	
	private otp_storage otpObj = new otp_storage();
    private final static String INCORRECT_OTP = "Incorrect Otp";
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
		
		jwt token = jwt_repo.findById(location.token).get();
		if(token == null) {
			throw new customError(TOKKEN_EXPIRED,HttpStatus.GATEWAY_TIMEOUT);

		}
		consumerdetails consumer  =  repo.findById(token.id).get();
		if(consumer == null) {
			throw new customError(EMAIL_NOT_FOUND,HttpStatus.NOT_FOUND);
		}
		consumer.consumer_location = location.newLocation;
		repo.save(consumer);
		
		token.expiry = getTime();
		jwt_repo.save(token);
		return ResponseEntity.ok(SUCCESS) ;
		
	}
	public ResponseEntity<String> register(consumerdetails newConsumer) throws customError {
		newConsumer.consumer_id = generateId();
		newConsumer.registration_date = getDate();
		consumerdetails consumer = repo.findByemail(newConsumer.email);
		if(consumer != null) {
			throw new customError(EMAIL_IN_USE,HttpStatus.CONFLICT);
		}
		repo.save(newConsumer);
		
		token.expiry = getTime();
		token.id = newConsumer.consumer_id;
		token.token = generateToken();
		jwt_repo.save(token);
		return ResponseEntity.ok(token.token);
	}
		public ResponseEntity<String> sendMail(String email) throws customError{
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
	       jwt_repo.save(token);
	       
	       
	       //saves otp in the database
	       otpObj.otp = otp;
	       otpObj.otp_expiry = getTime();
	       otpObj.token = token.token;
	       otpObj.token_expiry = token.expiry;
	       repo_otp.save(otpObj);
	       
	       deleteToken();
			return ResponseEntity.ok(token.token);
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
		
		jwt_repo.save(token);
		
		
		deleteToken();
		return  ResponseEntity.ok(token.token);
	}
		@Override
	public ResponseEntity<String> verify(otp obj) throws customError {
		otp_storage newOtpObj = repo_otp.findBytoken(obj.token);
		
		if(newOtpObj == null || !newOtpObj.otp.equals(obj.otp)) {
			throw new customError(INCORRECT_OTP, HttpStatus.BAD_REQUEST);

		}
		jwt token = jwt_repo.findById(obj.token).get();
		
		if(obj.token.equals(token.token)) {
			token.expiry = getTime();
			jwt_repo.save(token);
		}else {
			throw new customError(TOKKEN_EXPIRED, HttpStatus.BAD_REQUEST);

		}
		repo_otp.delete(newOtpObj);
		  deleteOtp();
		return ResponseEntity.ok(obj.token);
		
		
		
	}
	
	@Override
	public ResponseEntity<String> update(Password obj) throws customError {
		// TODO Auto-generated method stub
		jwt token = null;
		try {
			token = jwt_repo.findById(obj.token).get();
		}
		catch (NoSuchElementException e) {
			throw new customError(TOKKEN_EXPIRED, HttpStatus.BAD_REQUEST);

		}
		
		consumerdetails consumer = repo.findById(token.id).get();
		if(consumer == null) {
			throw new customError(USER_NOT_FOUND, HttpStatus.NOT_FOUND);

		}
		consumer.password = obj.newPassword;
		repo.save(consumer);
		token.expiry = getTime();
		token.type = "OPERATION";
		jwt_repo.save(token);
		return ResponseEntity.ok(token.token);
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
		
		String otp = "";
		for(int i = 0;i<6;i++) {
			otp = otp + rand.nextInt(num.length());
		}
		return otp;
	}
	private void deleteOtp() {
		List<otp_storage> otpStorage = repo_otp.findAll();
		for(int i =0;i<otpStorage.size();i++) {
			otp_storage otps = otpStorage.get(i);
			
			String hour = otps.otp_expiry.substring(0,2);
			  String currentHour = getTime().substring(0,2);
			  if(Integer.parseInt(hour) - Integer.parseInt(currentHour) != 0) {
				  repo_otp.delete(otps);
			  }
			  
		}
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
		List<jwt> tokens = jwt_repo.findAll();
	  for(int i =0;i<tokens.size();i++) {
		  jwt token = tokens.get(i);
		  String hour = token.expiry.substring(0,2);
		  String currentHour = getTime().substring(0,2);
		  if(Integer.parseInt(hour) - Integer.parseInt(currentHour) != 0) {
			  jwt_repo.delete(token);
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
