package com.Local.api.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Local.api.entities.orders;
@Repository
public interface orderRepo extends JpaRepository<orders,Integer> {

}
