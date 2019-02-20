package com.bridgelabz.fundoonotes.service;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.bridgelabz.fundoonotes.model.UserDetails;
import com.bridgelabz.fundoonotes.utility.EmailUtil;
import com.bridgelabz.fundoonotes.utility.TokenGenerator;
import com.bridgelabz.fundoonotes.controller.UserController;
import com.bridgelabz.fundoonotes.dao.UserDetailsRepository;

@Service
public class UserServiceImpl implements UserService {
	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserDetailsRepository userDetailsRepository;

	@Autowired
	private TokenGenerator tokenGenerator;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Autowired
	private EmailUtil emailUtil;

	@Override
	public UserDetails register(UserDetails user, HttpServletRequest request) {

		user.setPassword(bcryptEncoder.encode(user.getPassword()));
		UserDetails newUser = userDetailsRepository.save(user);
		if (newUser == null) {
			return null;
		}
		String token = tokenGenerator.generateToken(String.valueOf(newUser.getId()));
		System.out.println("token is" + token);
		StringBuffer requestUrl = request.getRequestURL();
		String activationUrl = requestUrl.substring(0, requestUrl.lastIndexOf("/"));
		activationUrl = activationUrl + "/activationstatus/" + token;
		emailUtil.sendEmail("", "", activationUrl);
		return userDetailsRepository.save(newUser);
	}

	@Override
	public UserDetails activateUser(String token, HttpServletRequest request) {
		int id = tokenGenerator.verifyToken(token);
		Optional<UserDetails> optional = userDetailsRepository.findById(id);
		return optional.map(user -> userDetailsRepository.save(user.setActivationStatus(true))).orElseGet(() -> null);
	}

	@Override
	public String login(UserDetails user) {
		UserDetails existingUser = userDetailsRepository.findAllByEmailId(user.getEmailId());
		logger.debug("user", existingUser);
		String token = null;
		boolean isMatch;
		if (existingUser != null) {
			isMatch = bcryptEncoder.matches(user.getPassword(), existingUser.getPassword());
			if (isMatch && existingUser.isActivationStatus()) {
				token = tokenGenerator.generateToken(String.valueOf(existingUser.getId()));
			}
		}
		return token;
	}
	
	
	
//	@Override
//	public String login(UserDetails user, HttpServletRequest request, HttpServletResponse response) {
//		UserDetails verifyUser = userDetailsRepository.findAllByEmailId(user.getEmailId());
//		if (bcryptEncoder.matches(user.getPassword(), verifyUser.getPassword()) && verifyUser.isActivationStatus())
//		{
//			String token = tokenGenerator.generateToken(String.valueOf(verifyUser.getId()));
//			return token;
//		}
//		return null;
//	}

	@Override
	public Optional<UserDetails> getUser(int id) {

		return userDetailsRepository.findById(id);
	}

	@Override
	public UserDetails update(String token, UserDetails user, HttpServletRequest request) {
		int userId = tokenGenerator.verifyToken(token);
		Optional<UserDetails> optional = userDetailsRepository.findById(userId);
		return optional.map(newUser -> userDetailsRepository.save(newUser.setEmailId(user.getEmailId())
				.setMobileNumber(user.getMobileNumber()).setName(user.getName()).setPassword(user.getPassword())))
				.orElseGet(() -> null);
		// if (optional.isPresent()) {
		// UserDetails newUser=optional.get();
		// newUser.setMobileNumber(user.getMobileNumber());
		// newUser.setName(user.getName());
		// newUser.setEmailId(user.getEmailId());
		// newUser.setPassword(user.getPassword());
		// newUser.setPassword(bcryptEncoder.encode(newUser.getPassword()));
		// userDetailsRepository.save(newUser);
		// return newUser;
		// }
		// return null;
	}

	@Override
	public boolean delete(String token, HttpServletRequest request) {
		int userId = tokenGenerator.verifyToken(token);
		Optional<UserDetails> optional = userDetailsRepository.findById(userId);
		if (optional.isPresent()) {
			UserDetails newUser = optional.get();
			userDetailsRepository.delete(newUser);
			return true;
		}
		return false;
	}

	@Override
	public boolean forgotPassword(String emailId, HttpServletRequest request) {
		UserDetails user1 = userDetailsRepository.findAllByEmailId(emailId);
		if (user1 != null) {
			String token = tokenGenerator.generateToken(String.valueOf(user1.getId()));
			StringBuffer requestUrl = request.getRequestURL();
			String activationUrl = requestUrl.substring(0, requestUrl.lastIndexOf("/"));
			activationUrl = activationUrl + "/resetpassword/" + token;
			emailUtil.sendEmail("", "Reset password verification", activationUrl);
			return true;
		}
		return false;
	}

	@Override
	public UserDetails resetPassword(UserDetails user, String token, HttpServletRequest request) {
		int id = tokenGenerator.verifyToken(token);
		Optional<UserDetails> optional = userDetailsRepository.findById(id);
		if (optional.isPresent()) {
			UserDetails user1 = optional.get();
			user1.setPassword(bcryptEncoder.encode(user1.getPassword()));
			userDetailsRepository.save(user1);
			return user1;
		}
		return null;
	}

}
