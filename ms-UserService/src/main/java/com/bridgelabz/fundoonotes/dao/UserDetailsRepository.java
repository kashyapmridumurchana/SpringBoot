package com.bridgelabz.fundoonotes.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoonotes.model.UserDetails;


@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer> {

	UserDetails findAllByEmailId(String emailId);
//
//	@Query(value = "SELECT user FROM mydb")
//	List<UserDetails> findAllUsers();



	
}
