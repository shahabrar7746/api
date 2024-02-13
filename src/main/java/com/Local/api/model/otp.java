package com.Local.api.model;
import org.springframework.*;
import org.springframework.context.annotation.Bean;

import lombok.Data;



public class otp {

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String otp,token;
	
}
