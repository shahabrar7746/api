package com.Local.api.service;

import com.Local.api.entities.sellerdetails;
import com.Local.api.model.serviceRequest;

public interface sellerInterface {
	public java.util.List<sellerdetails> findAllSeller();
	public java.util.List<sellerdetails> getService(serviceRequest request);
}
