package com.Local.api.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;

@Entity
@Data

public class orders {
    @Id
	public int id;
	public String Consumerid;
	public String Sellerid; 
	public String time; 
	public String date;
	public String status;
	public String message;
	
	
	
	
	
	
	
	
	
}
