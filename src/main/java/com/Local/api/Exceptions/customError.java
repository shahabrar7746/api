package com.Local.api.Exceptions;

import org.springframework.http.HttpStatus;

public class customError extends Exception {
	
	public customError(String message) {
		super(message);
	}
	
	public customError(String message,HttpStatus status) {
		super(message);
		this.status = status;
		
		
	}
	
	
	private HttpStatus status = HttpStatus.BAD_REQUEST;
	public HttpStatus getStatus() {
		return status;
	}
}
