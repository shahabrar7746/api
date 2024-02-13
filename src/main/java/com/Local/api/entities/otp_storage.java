package com.Local.api.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class otp_storage {

	
	@Id
	
	
	public String otp;
	
	public String token;
	
	public String token_expiry;
	
	public String otp_expiry;
	
}
