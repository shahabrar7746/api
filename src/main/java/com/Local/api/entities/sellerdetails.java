package com.Local.api.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Data
@Entity


@Table(name = "sellerdetails")
public class sellerdetails {
	
	
	@Id
	public String seller_id;
	 public String pin_code,seller_name,phone_number,service_name,password,state,shop_number,area,registration_date,service_type,email;

}
