package com.bridgelabz.fundoonotes.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bridgelabz.fundoonotes.dao.UserDetailsRepository;
import com.bridgelabz.fundoonotes.model.UserDetails;
import com.bridgelabz.fundoonotes.service.UserService;


@Controller
@RequestMapping("/user/")
public class UserController
{
	 @Autowired  
	    private UserService userService;  
	 
	 @Autowired 
	 private UserDetailsRepository userDetailsRepository;
	 
	 @PostMapping(value = "register")
		public ResponseEntity<?> register(@RequestBody UserDetails user, HttpServletRequest request) {

			UserDetails newUser = userService.register(user,request);
			if (newUser != null) {
				return new ResponseEntity<UserDetails>(newUser, HttpStatus.FOUND);
			} else {
				return new ResponseEntity<String>("Not created", HttpStatus.NOT_FOUND);
			}
		}
	    
	 
	 @GetMapping(value = "activationstatus/{token:.+}")
	    public ResponseEntity<?> activateUser(@PathVariable("token") String token, HttpServletRequest request) {

	        UserDetails user = userService.activateUser(token, request);
	        if (user != null) {
	            return new ResponseEntity<String>("Activated", HttpStatus.FOUND);
	        } else {
	            return new ResponseEntity<String>("Email incorrect. Please enter valid email address present in database",
	                    HttpStatus.NOT_FOUND);
	        }
	    }


	 
	 @PostMapping(value = "login")
		public ResponseEntity<?> loginUser(@RequestBody UserDetails user, HttpServletRequest request,
				HttpServletResponse response) {

			UserDetails newUser = userService.login(user, request, response);
			if (newUser != null) {
				return new ResponseEntity<UserDetails>(newUser, HttpStatus.FOUND);
			} else {
				return new ResponseEntity<String>("Incorrect emailId or password", HttpStatus.NOT_FOUND);
			}
		}
	   
	 
	 @PutMapping(value = "update")
		public ResponseEntity<?> update(@RequestHeader("token") String token, @RequestBody UserDetails user,
				HttpServletRequest request) {

			UserDetails newUser = userService.update(token, user, request);
			if (newUser != null) {
				return new ResponseEntity<UserDetails>(newUser, HttpStatus.FOUND);
			} else {
				return new ResponseEntity<String>("Email incorrect. Please enter valid email address present in database",
						HttpStatus.NOT_FOUND);
			}
		}
	 
	 
	 @DeleteMapping(value = "delete")
	 public ResponseEntity<?> delete(@RequestHeader("token") String token, HttpServletRequest request) {

			if (userService.delete(token, request)) {
				return new ResponseEntity<UserDetails>(HttpStatus.FOUND);
			} else {
				return new ResponseEntity<String>("Email incorrect. Please enter valid email address present in database",
						HttpStatus.NOT_FOUND);
			}
		}
	 
	 @PostMapping(value = "forgotpassword")
		public ResponseEntity<?> forgotpassword(@RequestParam("emailId") String emailId, HttpServletRequest request) {
			if (userService.forgotPassword(emailId, request)) {
				return new ResponseEntity<String>("Link sent to your emailId reset your password",
						HttpStatus.FOUND);
			} else {
				return new ResponseEntity<String>("Email incorrect. Please enter valid email address present in database",
						HttpStatus.NOT_FOUND);
			}	
		}
	 
		@PutMapping(value = "resetpassword/{token:.+}")
		public ResponseEntity<?> resetpassword(@RequestBody UserDetails user, @PathVariable("token") String token,
				HttpServletRequest request) {
			UserDetails newUser = userService.resetPassword(user, token, request);
			if (newUser != null) {
				return new ResponseEntity<String>("Password reset", HttpStatus.FOUND);
			} else {
				return new ResponseEntity<String>("password couldn't be reset", HttpStatus.NOT_FOUND);
			}
		} 
	 
}  

