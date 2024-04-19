package com.Local.api.service;


import com.Local.api.model.login;
import com.Local.api.model.orderBody;

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
import com.Local.api.repository.orderRepo;
import com.Local.api.repository.otpRepo;
import com.Local.api.repository.sellerRepo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
	private orderRepo ordersRepo;
	
	@Autowired
	private sellerRepo repo;
	@Autowired
	private consumerRepo repo2;
	
	@Autowired
	private jwtRepo jwt_repo;
	
	@Autowired
	private otpRepo repo_otp;
	
	private final jwt token = new jwt();

	private String WRONG_ORDER_ID = "Provided Order does not found to be true";
	
	private String EMAIL_SUBJECT = "Status of Request changed.";
	private String ORDER_MISMATCH = "Your Requested Order conflicts with other orders";
	private String SELLER_NOT_FOUND = "Seller Does not found";
	private String SUBJECT_FOR_ORDER_REQUEST = "Looks like Someone Called you!!";
	private String BODY_FOR_ORDER_REQUEST = "Someone requested for Listed Service on 24Local. Do checkout website for more details";
	private String ORDER_NOT_FOUND = "Your Requested Order does not exists";
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
	@Override
	public ResponseEntity<String> bookOrder(com.Local.api.model.bookOrder order) throws customError {
		// TODO Auto-generated method stub
		jwt tokken = jwt_repo.findBytoken(order.token);
		if(tokken == null) {
			throw new customError(TOKKEN_EXPIRED, HttpStatus.BAD_REQUEST);

		}
		tokken.expiry = getTime();
		
		int orderSize = ordersRepo.findAll().size();
		orders orderService = new orders();
		orderService.id = orderSize+ 1;
		orderService.date = getDate();
		orderService.time = getTime();
		orderService.Sellerid = order.id;
		orderService.Consumerid = tokken.id;
		orderService.message = order.message;
		orderService.status = "REQUESTED";
		ordersRepo.save(orderService);
		jwt_repo.save(tokken);
		
		
		if(!repo.findById(order.id).isPresent()) {
			throw new customError(SELLER_NOT_FOUND,HttpStatus.NOT_FOUND);
		}
		sellerdetails seller = repo.findById(order.id).get();
		
		sendNotification(seller.email,SUBJECT_FOR_ORDER_REQUEST,BODY_FOR_ORDER_REQUEST);
		return ResponseEntity.ok(tokken.token);
	}
	public ResponseEntity<String> login(login credentials) throws customError{
		sellerdetails seller = repo.findByemail(credentials.emailORnumber);
		if(seller == null) {
			throw new customError(EMAIL_NOT_FOUND,HttpStatus.NOT_FOUND);
		}
		if(!seller.password.equals(credentials.password)) {
			throw new customError(INCORRECT_PASSWORD,HttpStatus.BAD_REQUEST);
		}
		jwt tokken = new jwt();
		tokken.expiry = getTime();
		tokken.id = seller.seller_id;
		tokken.token = generateToken();
		tokken.type = "OPERATION";
		jwt_repo.save(tokken);
		return ResponseEntity.ok(tokken.token);
	}
	public List<orderBody> getOrders(String tokken) throws customError{
		jwt tokkenObj = jwt_repo.findBytoken(tokken);
		if(tokkenObj == null) {
			throw new customError(TOKKEN_EXPIRED, HttpStatus.BAD_REQUEST);
		}
		List<orderBody> orderBodyList = new LinkedList<>();
		List<orders> orderList = ordersRepo.findAll();
		for(int i =0;i<orderList.size();i++) {
			if(tokkenObj.id.equals(orderList.get(i).Sellerid)) {
				consumerdetails consumer = repo2.findById(orderList.get(i).Consumerid).get();
				orderBody body = new orderBody();
				body.address = consumer.consumer_location + ", " + consumer.pin_code;
				body.consumerName = consumer.name;
				body.email = consumer.email;
				body.date = orderList.get(i).date;
				body.message = orderList.get(i).message;
				body.time = orderList.get(i).time;
				body.id = orderList.get(i).id;
				orderBodyList.add(body);
			}
		}
		
		tokkenObj.expiry = getTime();
		jwt_repo.save(tokkenObj);
		deleteToken();
		return orderBodyList;
	}
	@Override
	public ResponseEntity<String> processRequest(com.Local.api.model.processRequest request) throws customError {
		// TODO Auto-generated method stub
		jwt tokkenObj = jwt_repo.findBytoken(request.tokken);
		//jwt tokkenObj = jwt_repo.findById(request.tokken);
		if(tokkenObj == null) {
			throw new customError(TOKKEN_EXPIRED, HttpStatus.BAD_REQUEST);

		}
		
		sellerdetails seller = repo.findById(tokkenObj.id).get();
		tokkenObj.expiry = getTime();
		jwt_repo.save(tokkenObj);
		if(!ordersRepo.findById(request.orderId).isPresent()) {
			throw new customError(ORDER_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
		orders curOrder = ordersRepo.findById(request.orderId).get();
		String body = "";
		if(!curOrder.Sellerid.equals(tokkenObj.id)) {
			throw new customError(ORDER_MISMATCH, HttpStatus.BAD_REQUEST);


		}
		if(request.isAccepted) {
			curOrder.status = "ACCEPTED";
		body = "Your Service Request, named as " + seller.service_name + " , requested on " +
			curOrder.date + " has beem Accepted. The service provider will contact you as soon as possible for further process " +
				"\n Thank you, \n 24Local Private Limited.";
			
			
			
		}else {
			body = "We regret to inform you that, your Service Request, named as " + seller.service_name + " , requested on " +
					curOrder.date + " has beem Rejected by service provider. The service provider will contact you as soon as possible for further process " +
						"\n Thank you, \n 24Local Private Limited.";
					
		curOrder.status = "REJECTED";
		}
		
		
		consumerdetails consumer = repo2.findById(curOrder.Consumerid).get();
		
		sendNotification(consumer.email,EMAIL_SUBJECT,body);
		ordersRepo.save(curOrder);
		deleteUnNecessayOrders();
		return ResponseEntity.ok(request.tokken);
	}
	
	
	
	private void deleteUnNecessayOrders() {
		List<orders> orderList = ordersRepo.findAll();
		
		for(int  i =0;i<orderList.size();i++) {
			orders curOrder = orderList.get(i);
			if(curOrder.status.equals("COMPLETED") || curOrder.status.equals("REJECTED")) {
			
				ordersRepo.delete(curOrder);
			}
		}
		
	}
	private void sendNotification(String recipient, String subject, String body) {
		 SimpleMailMessage message = new SimpleMailMessage();
		  message.setTo(recipient);
		  message.setSubject(subject);
		   message.setText(body);
		   emailSender.send(message);
	}
}
