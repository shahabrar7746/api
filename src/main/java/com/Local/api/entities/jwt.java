package com.Local.api.entities;

import java.beans.JavaBean;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class jwt {

	
	
	@Id
	public String token;
	public String id,expiry;

	
	public String type = "OPERATION";
}
