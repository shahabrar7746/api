package com.Local.api.Exceptions;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.Local.api.model.errorMessage;

@ControllerAdvice
@ResponseStatus


public class controllerExceptionHandler extends ResponseEntityExceptionHandler {
	
	
	@ExceptionHandler
	public ResponseEntity<errorMessage> throwError(customError error,WebRequest req){
		
		errorMessage message = new errorMessage(error.getStatus(),error.getMessage());
		return ResponseEntity.status(error.getStatus()).body(message);
	}

}
