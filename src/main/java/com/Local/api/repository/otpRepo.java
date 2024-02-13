package com.Local.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Local.api.entities.otp_storage;
import com.Local.api.entities.sellerdetails;

@Repository
public interface otpRepo extends JpaRepository<otp_storage,Integer> {
public otp_storage findBytoken(String token);
}
