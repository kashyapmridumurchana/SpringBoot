package com.bridgelabz.fundoonotes.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.fundoonotes.model.UserDetails;
import com.bridgelabz.fundoonotes.service.UserService;
import com.google.common.collect.MultimapBuilder.MultimapBuilderWithKeys;


@Controller
@RequestMapping("/user/")
public class UserController
{
	
	  private static Logger logger = LoggerFactory.getLogger(UserController.class);
	  
	  //log.info("details to be printed);
	
	 @Autowired  
	    private UserService userService;  
	 
	 
	 @Autowired
		@Qualifier("userValidation")
		private Validator validator;

	 @InitBinder
		private void initBinder(WebDataBinder binder) {
			binder.setValidator(validator);
		}
	
	 @PostMapping(value = "register")
		public ResponseEntity<?> register( @RequestBody UserDetails user,BindingResult bindingResult ,HttpServletRequest request) {
		 if (bindingResult.hasErrors())
		 {
				return new ResponseEntity<String>("Please enter valid details", HttpStatus.CONFLICT);
		} else 
		{
			UserDetails newUser = userService.register(user,request);
			if (newUser != null) 
			{
				return new ResponseEntity<UserDetails>(newUser, HttpStatus.OK);
			} else
			{
				return new ResponseEntity<String>("Not created", HttpStatus.NOT_FOUND);
			}
		}
		}
	    
	 
	 @GetMapping(value = "activationstatus/{token:.+}")
	    public ResponseEntity<?> activateUser(@PathVariable("token") String token, HttpServletRequest request) {

	        UserDetails user = userService.activateUser(token, request);
	        if (user != null) {
	            return new ResponseEntity<String>("Activated", HttpStatus.OK);
	        } else {
	            return new ResponseEntity<String>("Email incorrect. Please enter valid email address present in database",
	                    HttpStatus.NOT_FOUND);
	        }
	    }


	 
	 @PostMapping(value = "login")
		public ResponseEntity<?> loginUser(@RequestBody UserDetails user, HttpServletRequest request,
				HttpServletResponse response) {
			String token = userService.login(user);
			if (token != null) {
				response.setHeader("token", token);
				return new ResponseEntity<Void>(HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("Incorrect emailId or password", HttpStatus.CONFLICT);
			}
		}
	   
	 
	 @PutMapping(value = "update")
		public ResponseEntity<?> update(@RequestHeader("token") String token, @RequestBody UserDetails user,
				HttpServletRequest request) {

			UserDetails newUser = userService.update(token, user, request);
			if (newUser != null) {
				return new ResponseEntity<UserDetails>(newUser, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("Email incorrect. Please enter valid email address present in database",
						HttpStatus.NOT_FOUND);
			}
		}
	 
	 
	 @DeleteMapping(value = "delete")
	 public ResponseEntity<?> delete(@RequestHeader("token") String token, HttpServletRequest request) {

			if (userService.delete(token, request)) {
				return new ResponseEntity<UserDetails>(HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("Email incorrect. Please enter valid email address present in database",
						HttpStatus.NOT_FOUND);
			}
		}
	 
	 @PostMapping(value = "forgotpassword")
		public ResponseEntity<?> forgotpassword(@RequestBody UserDetails user, HttpServletRequest request) {
			if (userService.forgotPassword(user, request)) {
				return new ResponseEntity<Void>(HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("Email incorrect. Please enter valid email address present in database",
						HttpStatus.NOT_FOUND);
			}	
		}
	 
		@PutMapping(value = "resetpassword/{token:.+}")
		public ResponseEntity<?> resetpassword(@RequestBody UserDetails user, @PathVariable("token") String token,
				HttpServletRequest request) {
			UserDetails newUser = userService.resetpassword(user, token, request);
			if (newUser != null) {
				return new ResponseEntity<Void>(HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("password couldn't be reset", HttpStatus.OK);
			}
		} 
		
		@PutMapping(value = "imageUpload/{token:.+}")
		public ResponseEntity<?> uploadImage(@PathVariable("token") String token,
				@RequestParam MultipartFile file) {
			UserDetails newUser = userService.imageSaving(token,file);
			if (newUser != null) {
				return new ResponseEntity<Void>(HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("photo couldnot be uploaded", HttpStatus.OK);
			}
		} 
		
		@DeleteMapping(value = "imagedelete/{token:.+}")
		 public ResponseEntity<?> deleteImage(@PathVariable("token") String token, HttpServletRequest request) {
     UserDetails user=userService.deleteImage(token, request);
				if (user!=null) {
					return new ResponseEntity<UserDetails>(HttpStatus.OK);
				} else {
					return new ResponseEntity<String>("error in deleting image",
							HttpStatus.NOT_FOUND);
				}
			}
		
	
	@GetMapping(value="userdetails")
	public ResponseEntity<?> collaborator(@RequestHeader("token") String token,
			HttpServletRequest request) {
		UserDetails newUser = userService.collaborator(token, request);
		if (newUser != null) {
			return new ResponseEntity<UserDetails>(newUser,HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>( HttpStatus.NOT_FOUND);
		}
	} 	
	
	@GetMapping(value="allUserDetails")
	public ResponseEntity<?> getAllUsers(HttpServletRequest request) {
		List<UserDetails> users = userService.getAllUsers( request);
		if (users != null) {
			return new ResponseEntity<List<UserDetails>>(users, HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>( HttpStatus.NOT_FOUND);
		}
	} 	
	@GetMapping(value = "verifyemail/{emailId:.+}")
	public ResponseEntity<?> verifyEmail(@RequestHeader("token") String token,@PathVariable("emailId") String email, HttpServletRequest request) {
		UserDetails newUser=userService.verifyEmail(token,email,request);
		if (newUser!=null)
			return new ResponseEntity<UserDetails>(newUser,HttpStatus.OK);
		return new ResponseEntity<String>("user not found", HttpStatus.NOT_FOUND);
}
	
}  

