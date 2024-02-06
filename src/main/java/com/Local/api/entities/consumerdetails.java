package com.Local.api.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity


@Table(name = "consumerdetails")
public class consumerdetails {
	
	
	@Id
	public String consumer_id;
	
	public String password;
	
	public String email;
	
	public String registration_date;
	
	public String consumer_location;
	
	public String name;
	
	public String pin_code;
	
	

}
