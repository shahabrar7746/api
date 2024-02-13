package com.Local.api.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Local.api.entities.jwt;

@Repository
public interface jwtRepo extends JpaRepository<jwt,String> {

}
