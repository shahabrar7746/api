package com.Local.api.service;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.Local.api.Exceptions.customError;
import com.Local.api.entities.*;
import com.Local.api.model.Password;
import com.Local.api.model.otp;
import com.Local.api.model.serviceRequest;
import com.Local.api.repository.consumerRepo;
import com.Local.api.repository.jwtRepo;
import com.Local.api.repository.otpRepo;
import com.Local.api.repository.sellerRepo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

//import java.util.List;
import ch.qos.logback.classic.turbo.ReconfigureOnChangeFilter;

@Service
public class sellerService implements sellerInterface{

	@Autowired
	 private JavaMailSender emailSender;
	
	@Autowired
	private sellerRepo repo;
	@Autowired
	private consumerRepo repo2;
	
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

	@Autowired
	private jwtRepo tokkens;
	
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



	
	public java.util.List<sellerdetails> findAllSeller(){
		return repo.findAll();
	}
	public java.util.List<sellerdetails> getService(serviceRequest request) throws customError{
		
		Optional<jwt> optionalTokken = tokkens.findById(request.token);
		
		if(!optionalTokken.isPresent()) {
		// throws error if  tokken is valid.
			throw new customError(TOKKEN_EXPIRED,HttpStatus.BAD_REQUEST);
		}
		//proceeds further if  tokken is valid.
		jwt tokken = optionalTokken.get();
		tokken.expiry = getTime();//updates tokken expiry;
		tokkens.save(tokken);
		request.query = request.query.toLowerCase();// converts requested query to lower case.
		String id = optionalTokken.get().id;		
		consumerdetails consumer = repo2.findById(id).get();
		java.util.List<sellerdetails> list = new LinkedList<>();
		java.util.List<sellerdetails> sellerList = repo.findAll();
		for(int i =0;i<sellerList.size();i++) {
			String curSellerServiceType = sellerList.get(i).service_type.toLowerCase();
			String curSellerServiceName = sellerList.get(i).service_name.toLowerCase();

			if(sellerList.get(i).pin_code.equals(consumer.pin_code) && (curSellerServiceType.contains(request.query) || curSellerServiceName.contains(request.query))) {
				list.add(sellerList.get(i));
			}
		}
		
		return list;
		
		
	}
	public ResponseEntity<String> register(sellerdetails seller) throws customError{
		seller.seller_id = generateId();
		sellerdetails registeredSeller = repo.findByemail(seller.email);
		if(registeredSeller != null) {
		throw new customError(EMAIL_IN_USE,HttpStatus.CONFLICT);	
		}
	//tokken reneration ans save
		seller.registration_date = getDate();
		token.expiry = getTime();
		token.id = seller.seller_id;
		token.token = generateToken();
		jwt_repo.save(token);
	
		repo.save(seller);
		
		//tokken cleaning
		deleteToken();
		return ResponseEntity.ok(token.token);
	}
	public ResponseEntity<String> changePassword(String email)  throws customError{
		sellerdetails curSeller = repo.findByemail(email);
		if(curSeller == null) {
			throw new customError(EMAIL_NOT_FOUND,HttpStatus.NOT_FOUND);
			
		}
		
		//sends email 
		String otp = getOtp();
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
      
		
		
		//saves token in db.
		token.expiry = getTime();
		token.id = curSeller.seller_id;
		token.token = generateToken();
		jwt_repo.save(token);
		
		//saves otp in db.
		
		otpObj.otp = otp;
		otpObj.otp_expiry = getTime();
		otpObj.token = token.token;
		otpObj.token_expiry = token.expiry;
		
		repo_otp.save(otpObj);
		return ResponseEntity.ok(token.token);
		
	}
	
	public ResponseEntity<String> verify(otp sentOtp) throws customError{
		//verify the otp.
		Optional option = jwt_repo.findById(sentOtp.token);
		
		jwt curTokken = null;
		if(option.isPresent()) {
			curTokken = (jwt) option.get();
		}
		if(curTokken == null) {
			throw new customError(TOKKEN_EXPIRED, HttpStatus.BAD_REQUEST);

		}
		
		sellerdetails curSeller = repo.findById(curTokken.id).get();
		otp_storage curOtp = repo_otp.findBytoken(sentOtp.token);
		if(!sentOtp.otp.equals(curOtp.otp)) {
			throw new customError(INCORRECT_OTP, HttpStatus.BAD_REQUEST);

		}
		
		curTokken.expiry = getTime();
		jwt_repo.save(curTokken);
		repo_otp.delete(curOtp);;
		deleteOtp();
		
		
		
		return ResponseEntity.ok(curTokken.token);
		
	}
	public ResponseEntity<String> reset(Password newPassword) throws customError{
	Optional option = jwt_repo.findById(newPassword.token);
	if(!option.isPresent()) {
		throw new customError(TOKKEN_EXPIRED, HttpStatus.BAD_REQUEST);

	}
	jwt curTokken = (jwt) option.get();
	if(!curTokken.token.equals(newPassword.token)) {
		throw new customError(TOKKEN_EXPIRED, HttpStatus.BAD_REQUEST);

	}
	sellerdetails curSeller = repo.findById(curTokken.id).get();
	curSeller.password = newPassword.newPassword;
	repo.save(curSeller);
	curTokken.expiry = getTime();
	jwt_repo.save(curTokken);
	return ResponseEntity.ok(curTokken.token);
	
	}
}
