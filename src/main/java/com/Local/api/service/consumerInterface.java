package com.Local.api.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.Local.api.Exceptions.customError;
import com.Local.api.entities.consumerdetails;
import com.Local.api.model.changeLocation;
import com.Local.api.model.login;
import com.Local.api.model.otp;
import com.Local.api.repository.consumerRepo;

public interface consumerInterface {
	
	public List<consumerdetails> findAllConsumers();
	public ResponseEntity<String>  changeLocation(changeLocation location) throws customError;
	public consumerdetails register(consumerdetails newConsumer) throws customError;
	public ResponseEntity<otp> sendMail(String email) throws customError;
	public ResponseEntity<String> doLogin(login logUser) throws customError;
}
