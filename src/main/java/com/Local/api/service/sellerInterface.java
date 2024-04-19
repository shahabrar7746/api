package com.Local.api.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import com.Local.api.model.*;

import com.Local.api.Exceptions.customError;
import com.Local.api.entities.sellerdetails;

public interface sellerInterface {
	public java.util.List<sellerdetails> findAllSeller();
	public java.util.List<sellerdetails> getService(serviceRequest request) throws customError;
	public ResponseEntity<String> register(sellerdetails seller) throws customError;
	public ResponseEntity<String> changePassword(String email)  throws customError;
	public ResponseEntity<String> verify(otp sentOtp) throws customError;
	public ResponseEntity<String> reset(Password newPassword) throws customError;
	public ResponseEntity<String> bookOrder(bookOrder order) throws customError;
	public ResponseEntity<String> login(login credentials) throws customError;
	public List<orderBody> getOrders(String tokken) throws customError;
	public ResponseEntity<String> processRequest(processRequest request) throws customError;

}
