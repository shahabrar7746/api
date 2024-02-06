package com.Local.api.entities;

import java.net.http.HttpResponse;

import org.springframework.http.HttpStatus;

import jakarta.persistence.Entity;
import lombok.Data;


@Data
public class errorMessage {

	public HttpStatus status;
    public String message;
    public errorMessage(HttpStatus status,String message){
  	  this.status = status;
  	  this.message = message;
    }
}
