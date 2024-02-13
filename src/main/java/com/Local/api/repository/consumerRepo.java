package com.Local.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.Local.api.Exceptions.customError;
import com.Local.api.entities.consumerdetails;
import com.Local.api.model.changeLocation;
import com.Local.api.model.login;
import com.Local.api.model.otp;

@Repository
public interface consumerRepo extends JpaRepository<consumerdetails,String> {

	public consumerdetails findByemail(String email);
	public List<consumerdetails> findAllConsumers();
	public ResponseEntity<String>  changeLocation(changeLocation location) throws customError;
	public consumerdetails register(consumerdetails newConsumer) throws customError;
	public ResponseEntity<otp> sendMail(String email) throws customError;
	public ResponseEntity<String> doLogin(login logUser);
}
