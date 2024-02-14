package com.Local.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.Local.api.Exceptions.customError;
import com.Local.api.entities.sellerdetails;
import com.Local.api.model.serviceRequest;
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
	public List<sellerdetails> getServices(@RequestBody serviceRequest request){
		return service.getService(request);
	}
	
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody sellerdetails seller) throws customError{
		return service.register(seller);
	}
}
