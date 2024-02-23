package com.Local.api.service;

import org.springframework.http.ResponseEntity;

import com.Local.api.Exceptions.customError;
import com.Local.api.entities.sellerdetails;
import com.Local.api.model.Password;
import com.Local.api.model.otp;
import com.Local.api.model.serviceRequest;

public interface sellerInterface {
	public java.util.List<sellerdetails> findAllSeller();
	public java.util.List<sellerdetails> getService(serviceRequest request) throws customError;
	public ResponseEntity<String> register(sellerdetails seller) throws customError;
	public ResponseEntity<String> changePassword(String email)  throws customError;
	public ResponseEntity<String> verify(otp sentOtp) throws customError;
	public ResponseEntity<String> reset(Password newPassword) throws customError;
}
