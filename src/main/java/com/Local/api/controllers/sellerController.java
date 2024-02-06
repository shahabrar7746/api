package com.Local.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Local.api.entities.sellerdetails;
import com.Local.api.service.sellerService;

@RestController
public class sellerController {

	@Autowired
	private sellerService service;
	
	@GetMapping("/sellers")
	public List<sellerdetails> findAllSeller(){
		return service.findAllSeller();
	}
}