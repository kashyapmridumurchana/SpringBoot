package com.bridgelabz.fundoonotes.service;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bridgelabz.fundoonotes.model.UserDetails;

public interface UserService {

	UserDetails register(UserDetails user,HttpServletRequest request);
	
	//String login(UserDetails user,HttpServletRequest request,HttpServletResponse response);

	Optional<UserDetails> getUser(int id);
	
	UserDetails activateUser(String token, HttpServletRequest request);

	 
	UserDetails update(String token,UserDetails user,HttpServletRequest request);

	boolean delete(String token,HttpServletRequest request);


	boolean forgotPassword(UserDetails user, HttpServletRequest request);

	UserDetails resetpassword(UserDetails user, String token, HttpServletRequest request);

	String login(UserDetails user);   
}
