package com.Local.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Local.api.entities.consumerdetails;

@Repository
public interface consumerRepo extends JpaRepository<consumerdetails,String> {

	
	
}
