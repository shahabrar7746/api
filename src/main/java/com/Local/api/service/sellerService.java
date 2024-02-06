package com.Local.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Local.api.entities.*;
import com.Local.api.repository.sellerRepo;

@Service
public class sellerService {

	
	
	@Autowired
	private sellerRepo repo;
	
	
	
	public List<sellerdetails> findAllSeller(){
		return repo.findAll();
	}
}
