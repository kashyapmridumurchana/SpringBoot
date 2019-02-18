package com.bridgelabz.fundoonotes.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoonotes.model.UserDetails;


@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer> {

	UserDetails findAllByEmailId(String emailId);



	
}
