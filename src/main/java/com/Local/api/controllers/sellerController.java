package com.Local.api.controllers;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.Local.api.Exceptions.customError;
import com.Local.api.entities.orders;
import com.Local.api.entities.sellerdetails;
import com.Local.api.model.Password;
import com.Local.api.model.bookOrder;
import com.Local.api.model.login;
import com.Local.api.model.orderBody;
import com.Local.api.model.otp;
import com.Local.api.model.processRequest;
import com.Local.api.model.serviceRequest;
import com.Local.api.repository.jwtRepo;
import com.Local.api.service.sellerService;

@RestController
public class sellerController {

	@Autowired
	private sellerService service;
	
	
	
	
	@GetMapping("/sellers")
	public List<sellerdetails> findAllSeller(){
		return service.findAllSeller();
	}
	@PostMapping("/service")
	public List<sellerdetails> getServices (@RequestBody serviceRequest request)throws customError{
		return service.getService(request);
	}
	
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody sellerdetails seller) throws customError{
		return service.register(seller);
	}
	@PostMapping("/changePasword/{email}")
	public ResponseEntity<String> changePassword(@PathVariable("email") String email)throws customError{
		return service.changePassword(email);
	}
	
	@PutMapping("/verify/otp")
	public ResponseEntity<String> verify(@RequestBody otp sentOtp)throws customError{
	  return service.verify(sentOtp);	
	}
	
	@PutMapping("/reset/password")
	public ResponseEntity<String> reset(@RequestBody Password newPassword) throws customError{
		return service.reset(newPassword);
	}
	@PostMapping("/book")
	public ResponseEntity<String> bookOrder(@RequestBody bookOrder order)throws customError{
		return service.bookOrder(order);
		
	}
	
	
	@PostMapping("/auth/login")
	public ResponseEntity<String> doLogin(@RequestBody login credentials) throws customError{
		return service.login(credentials);
	}
	
	@GetMapping("/orders/{tokken}")
	public List<orderBody> getOrders(@PathVariable("tokken") String tokken) throws customError{
		return service.getOrders(tokken);
	}
	
	
	@PostMapping("/accept/order")
	public ResponseEntity<String> processOrder(@RequestBody processRequest request) throws customError{
		return service.processRequest(request);
	}
}
