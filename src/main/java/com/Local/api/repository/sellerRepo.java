package com.Local.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Local.api.entities.sellerdetails;
@Repository

public interface sellerRepo extends JpaRepository<sellerdetails,String>{
 
	
	
	
}
