package com.bridgelabz.fundoonotes.utility;

import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bridgelabz.fundoonotes.controller.UserController;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Component
public class JWTGenerator implements TokenGenerator
{
	 private static Logger logger = LoggerFactory.getLogger(UserController.class);
	public String generateToken(String id) {
		return Jwts.builder().setId(id).claim("roles", "existingUser").setIssuedAt(new Date())
				.signWith(SignatureAlgorithm.HS256, "secretKey").compact();
	}

	public int verifyToken(String token) {
		Claims claims = Jwts.parser()        
             .setSigningKey(DatatypeConverter.parseBase64Binary("secretKey"))
             .parseClaimsJws(token).getBody();
		logger.info("ID: " + claims.getId());
            // System.out.println("ID: " + claims.getId());
             return Integer.parseInt(claims.getId());
}
	
	
	

}
