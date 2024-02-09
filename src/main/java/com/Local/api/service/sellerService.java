package com.Local.api.service;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Local.api.entities.*;
import com.Local.api.model.serviceRequest;
import com.Local.api.repository.consumerRepo;
import com.Local.api.repository.sellerRepo;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
//import java.util.List;
import ch.qos.logback.classic.turbo.ReconfigureOnChangeFilter;

@Service
public class sellerService {

	
	
	@Autowired
	private sellerRepo repo;
	@Autowired
	private consumerRepo repo2;
	
	
	public java.util.List<sellerdetails> findAllSeller(){
		return repo.findAll();
	}
	public java.util.List<sellerdetails> getService(serviceRequest request){
		
		
		consumerdetails consumer = repo2.findById(request.id).get();
		java.util.List<sellerdetails> list = new LinkedList<>();
		java.util.List<sellerdetails> sellerList = repo.findAll();
		for(int i =0;i<sellerList.size();i++) {
			
			if(sellerList.get(i).pin_code.equals(consumer.pin_code) && sellerList.get(i).service_type.equals(request.query)) {
				list.add(sellerList.get(i));
			}
		}
		
		return list;
		
		
	}
}
