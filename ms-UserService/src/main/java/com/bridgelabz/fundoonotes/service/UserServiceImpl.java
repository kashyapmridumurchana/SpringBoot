package com.bridgelabz.fundoonotes.service;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.bridgelabz.fundoonotes.model.UserDetails;
import com.bridgelabz.fundoonotes.utility.EmailUtil;
import com.bridgelabz.fundoonotes.utility.TokenGenerator;
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
		sendEmail(request, newUser, "/activationstatus/", "Fundoo Note Verification");
         return user;
	}

	
	private void sendEmail(HttpServletRequest request, UserDetails user, String domainUrl, String message) {
		String token = tokenGenerator.generateToken(String.valueOf(user.getId()));
		StringBuffer requestUrl = request.getRequestURL();
		String activationUrl = requestUrl.substring(0, requestUrl.lastIndexOf("/"));
		activationUrl += domainUrl + token;
		emailUtil.sendEmail("", message, activationUrl);
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
	public boolean forgotPassword(UserDetails user, HttpServletRequest request) {
		UserDetails user1 = userDetailsRepository.findAllByEmailId(user.getEmailId());
		if (user1 != null) {
			String token = tokenGenerator.generateToken(String.valueOf(user1.getId()));
//			StringBuffer requestUrl = request.getRequestURL();
//			String activationUrl = requestUrl.substring(0, requestUrl.lastIndexOf("/"));
//			activationUrl = activationUrl + "/resetpassword/" + token;
			String forgorPasswordUrl="http://localhost:4200/resetpassword/" +token;
			emailUtil.sendEmail("", "",forgorPasswordUrl);
			return true;
		}
		return false;
	}

	@Override
	public UserDetails resetpassword(UserDetails user, String token, HttpServletRequest request) {
		int id = tokenGenerator.verifyToken(token);
		Optional<UserDetails> optional = userDetailsRepository.findById(id);
		if (optional.isPresent()) {
			UserDetails user1 = optional.get();
			user1.setPassword(bcryptEncoder.encode(user.getPassword()));
			userDetailsRepository.save(user1);
			return user1;
		}
		return null;
	}


	@Override
	public UserDetails collaborator(String token, HttpServletRequest request) {
		int id = tokenGenerator.verifyToken(token);
		Optional<UserDetails> optional = userDetailsRepository.findById(id);
		if (optional.isPresent()) {
			UserDetails user1 = optional.get();
			return user1;
		}
		return null;
	}

}
